package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.gpu.positionOf
import org.waoss.k8.input.byte
import org.waoss.k8.input.key
import org.waoss.k8.logger

interface ExecutionEngine : Loggable {
    fun execute(instruction: Instruction): Boolean
    val digestMap: Map<String, (Instruction) -> Boolean>
    val processorContext: ProcessorContext
}

operator fun Byte.get(index: Int): Boolean {
    return ((this and (1 shl index).toByte()) == (1 shl index).toByte())
}

fun ProcessorContext.lazyExecutionEngine(): Lazy<ExecutionEngine> = lazy {
    ExecutionEngineImpl(this@lazyExecutionEngine)
}

internal class ExecutionEngineImpl(override val processorContext: ProcessorContext) : ExecutionEngine {

    private fun withContext(digest: ProcessorContext.() -> Unit) {
        processorContext.digest()
    }

    private fun ProcessorContext.updateXWithOperated(instruction: Instruction, operation: (Byte, Byte) -> Byte) {
        val x = instruction.args[0].toInt()
        val y = instruction.args[1].toInt()
        generalPurposeRegisterBank[x] = operation(generalPurposeRegisterBank[x], generalPurposeRegisterBank[y])
    }

    private fun ProcessorContext.skipNext() {
        logger.info("Skipping next instruction")
        instructionPointer.apply {
            value = (value + 2).toShort()
        }
    }

    override fun execute(instruction: Instruction): Boolean {
        // executes iff the lambda exists and is non-null
        return if (digestMap[instruction.name] != null) {
            if (instruction != emptyInstruction()) {
                logger.info("Executing instruction $instruction")
            } else {
                logger.info("Encountered empty instruction, performing no-op")
            }
            digestMap[instruction.name]?.let { it(instruction) } ?: false
        } else {
            logger.error("No digest exists for instruction ${instruction.name}")
            false
        }
    }

    override val digestMap: Map<String, (Instruction) -> Boolean> = mutableMapOf(
        "00E0" to fun(_: Instruction): Boolean {
            withContext { graphicsContext.clearScreen() }
            return false
        }, // cls
        "00EE" to fun(_: Instruction): Boolean { // ret from subroutine
            withContext {
                instructionPointer.value = stackMemory[stackPointer.value.toInt()]
                stackPointer.value = (stackPointer.value - 1).toByte()
            }
            return true
        },
        "1NNN" to fun(it: Instruction): Boolean { // jump to `NNN` address
            withContext {
                instructionPointer.value = (it.args[0].toInt() and 0x0fff).toShort()
            }
            return true
        },
        "2NNN" to fun(it: Instruction): Boolean { // call `NNN` address
            withContext {
                stackPointer.value = (stackPointer.value + 1).toByte()
                stackMemory[stackPointer.value.toInt()] = instructionPointer.value
                instructionPointer.value = it.args[0]
            }
            return true
        },
        "3XKK" to fun(it: Instruction): Boolean { // SE Vx, skip next iff V[x] == kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] == kk.toByte()) {
                    skipNext()
                }
            }
            return true
        },
        "4XKK" to fun(it: Instruction): Boolean { // SNE Vx, skip next iff V[x] != kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] != kk.toByte()) {
                    skipNext()
                }
            }
            return true
        },
        "5XY0" to fun(it: Instruction): Boolean { // SE Vx, Vy, skip next iff V[x] == V[y]
            withContext {
                val x = it.args[0]
                val y = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] == generalPurposeRegisterBank[y.toInt()]) {
                    skipNext()
                }
            }
            return true
        },
        "6XKK" to fun(it: Instruction): Boolean { // LD Vx, load `kk` into V[x] register
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                generalPurposeRegisterBank[x.toInt()] = kk.toByte()
            }
            return false
        },
        "7XKK" to fun(it: Instruction): Boolean { // ADD Vx, => V[x] += kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                generalPurposeRegisterBank[x.toInt()] = (generalPurposeRegisterBank[x.toInt()] + kk).toByte()
            }
            return false
        },
        "8XY0" to fun(it: Instruction): Boolean { // LD XY, assigns the value of V[y] to V[x]
            withContext {
                updateXWithOperated(it) { _, y -> y }
            }
            return false
        },
        "8XY1" to fun(it: Instruction): Boolean { // X = X bitwise-or Y
            withContext {
                updateXWithOperated(it) { x, y -> x or y }
            }
            return false
        },
        "8XY2" to fun(it: Instruction): Boolean { // X = X bitwise-and Y
            withContext {
                updateXWithOperated(it) { x, y -> x and y }
            }
            return false
        },
        "8XY3" to fun(it: Instruction): Boolean { // X = X bitwise-xor Y
            withContext {
                updateXWithOperated(it) { x, y -> x xor y }
            }
            return false
        },
        "8XY4" to fun(it: Instruction): Boolean { // ADD XY, add V[x] and V[y] and set it on V[x]
            withContext {
                updateXWithOperated(it) { x, y -> (x + y).toByte() }
                val x = generalPurposeRegisterBank[it.args[0].toInt()].toInt()
                val y = generalPurposeRegisterBank[it.args[1].toInt()].toInt()
                val intSum = (x + y) // Kotlin automatically typecasts Byte operations to Int
                if (intSum < x || intSum < y) {
                    // overflow has taken place
                    generalPurposeRegisterBank[0xf] = 1
                } else {
                    generalPurposeRegisterBank[0xf] = 0
                }
            }
            return false
        },
        "8XY5" to fun(it: Instruction): Boolean { // SUB XY, V[x] = V[x] - V[y], if V[x] > V[y], V[F] = 1
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()].toInt()
                val yValue = generalPurposeRegisterBank[it.args[1].toInt()].toInt()
                updateXWithOperated(it) { x, y -> (x - y).toByte() }
                if (xValue > yValue) {
                    generalPurposeRegisterBank[0xf] = 1
                } else {
                    generalPurposeRegisterBank[0xf] = 0
                }
            }
            return false
        },
        "8XY6" to fun(it: Instruction): Boolean { // V[x] >>>= 1, V[y] is ignored
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()]
                generalPurposeRegisterBank[0xf] = xValue.leastSignificantBit
                updateXWithOperated(it) { x, _ -> (x.toInt() ushr 1).toByte() }
            }
            return false
        },
        "8XY7" to fun(it: Instruction): Boolean { // SUBN XY, V[x] = V[y] - V[x], if V[y] > V[x], V[F] = 1
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()].toInt()
                val yValue = generalPurposeRegisterBank[it.args[1].toInt()].toInt()
                updateXWithOperated(it) { x, y -> (y - x).toByte() }
                if (yValue > xValue) {
                    generalPurposeRegisterBank[0xf] = 1
                } else {
                    generalPurposeRegisterBank[0xf] = 0
                }
            }
            return false
        },
        "8XYE" to fun(it: Instruction): Boolean { // V[x] <<= 1, set V[F] as most significant bit of V[x] before left-shift
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()]
                if (xValue.mostSignificantBit == 1.toByte()) {
                    generalPurposeRegisterBank[0xf] = 1
                }
                updateXWithOperated(it) { x, _ -> (x.toInt() shl 1).toByte() }
            }
            return false
        },
        "9XY0" to fun(it: Instruction): Boolean { // SNE XY, skip next if V[x] != V[y]
            withContext {
                val x = it.args[0].toInt()
                val y = it.args[1].toInt()
                if (generalPurposeRegisterBank[x] != generalPurposeRegisterBank[y]) {
                    skipNext()
                }
            }
            return false
        },
        "ANNN" to fun(it: Instruction): Boolean { // set I as `NNN`
            withContext {
                iRegister.value = it.args[0]
            }
            return false
        },
        "BNNN" to fun(it: Instruction): Boolean { // jump to location `NNN` + V[0]
            withContext {
                instructionPointer.apply {
                    value = (it.args[0] + generalPurposeRegisterBank[0]).toShort()
                }
            }
            return true
        },
        "CXKK" to fun(it: Instruction): Boolean {
            withContext {
                val random = (0..255).random()
                val x = it.args[0].toInt()
                val kk = it.args[1].toInt()
                generalPurposeRegisterBank[x] = (random and kk).toByte()
            }
            return false
        },
        "FX07" to fun(it: Instruction): Boolean {
            withContext {
                val x = it.args[0].toInt()
                generalPurposeRegisterBank[x] = delayTimer.value
            }
            return false
        },
        "FX15" to fun(it: Instruction): Boolean {
            withContext {
                val x = it.args[0].toInt()
                delayTimer.value = generalPurposeRegisterBank[x]
            }
            return false
        },
        "FX18" to fun(it: Instruction): Boolean {
            withContext {
                val x = it.args[0].toInt()
                soundTimer.value = generalPurposeRegisterBank[x]
            }
            return false
        },
        "FX1E" to fun(it: Instruction): Boolean { // I += V[x]
            withContext {
                iRegister.apply {
                    val x = it.args[0].toInt()
                    val unsignedVx = generalPurposeRegisterBank[x].toUInt()
                    val unsignedI = this.value.toUInt()
                    this.value = (unsignedI + unsignedVx).toShort()
                }
            }
            return false
        },
        "FX29" to fun(it: Instruction): Boolean { // point I to the hexadecimal sprite address of [x]
            withContext {
                val x = it.args[0].toInt()
                iRegister.value = graphicsContext.hexadecimalSpriteAddress(x)
            }
            return false
        },
        "FX33" to fun(it: Instruction): Boolean { // load V[x], convert to BCD, and store at [I, I + 1, I + 2] as [H, T, O]
            withContext {
                val xValue = it.args[0].toUByte()
                iRegister.value.toInt().apply {
                    generalMemory[this] = xValue.hundreds.toByte()
                    generalMemory[this + 1] = xValue.tens.toByte()
                    generalMemory[this + 2] = xValue.ones.toByte()
                }
            }
            return false
        },
        "FX55" to fun(it: Instruction): Boolean { // store the registers into the memory at I
            withContext {
                val x = it.args[0]
                for (i in 0..x) {
                    generalMemory[iRegister.value + i] = generalPurposeRegisterBank[i]
                }
            }
            return false
        },
        "FX65" to fun(it: Instruction): Boolean { // load the registers from memory at I
            withContext {
                val x = it.args[0]
                for (i in 0..x) {
                    generalPurposeRegisterBank[i] = generalMemory[iRegister.value + i]
                }
            }
            return false
        },
        "DXYN" to fun(it: Instruction): Boolean {
            withContext {
                val x = generalPurposeRegisterBank[it.args[0].toInt()]
                val y = generalPurposeRegisterBank[it.args[1].toInt()]
                val n = it.args[2].toInt()
                for (i in 0 until n) {
                    val read = generalMemory[iRegister.value + i]
                    // TODO: Read more docs about the DXYN instruction
                    for (j in 0 until 8) {
                        val position = positionOf(x + j, y + i)
                        val previous = graphicsContext[position]
                        val new = previous xor read[7 - j]
                        graphicsContext.apply {
                            if (new) draw(position, 1)
                            else draw(position, 0)
                        }
                        if (previous && !new) {
                            generalPurposeRegisterBank[0xf] = 1
                        }
                    }
                }
            }
            return false
        },
        "EX9E" to fun(it: Instruction): Boolean {
            var toReturn = false
            withContext {
                val x = it.args[0].toInt()
                val key = (generalPurposeRegisterBank[x] and 0x0f).key
                if (keyboard.isPressed(key)) {
                    skipNext()
                    toReturn = true
                }
            }
            return toReturn
        },
        "EXA1" to fun(it: Instruction): Boolean {
            var toReturn = false
            withContext {
                val x = it.args[0].toInt()
                val key = (generalPurposeRegisterBank[x] and 0x0f).key
                if (!keyboard.isPressed(key)) {
                    skipNext()
                    toReturn = true
                }
            }
            return toReturn
        },
        "FX0A" to fun(it: Instruction): Boolean {
            withContext {
                val key = keyboard.nextKey()
                val x = it.args[0].toInt()
                generalPurposeRegisterBank[x] = key.byte
            }
            return false
        }
    )
}