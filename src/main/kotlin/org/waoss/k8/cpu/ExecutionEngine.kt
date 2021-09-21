package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.logger

interface ExecutionEngine : Loggable {
    fun execute(instruction: Instruction)
    val digestMap: Map<String, (Instruction) -> Unit>
    val context: Context
}

internal class ExecutionEngineImpl(override val context: Context) : ExecutionEngine {

    private fun withContext(digest: Context.() -> Unit) {
        context.digest()
    }

    private fun Context.updateXWithOperated(instruction: Instruction, operation: (Byte, Byte) -> Byte) {
        val x = instruction.args[0].toInt()
        val y = instruction.args[1].toInt()
        generalPurposeRegisterBank[x] = operation(generalPurposeRegisterBank[x], generalPurposeRegisterBank[y])
    }

    private fun Context.skipNext() {
        stackPointerRegisterBank.apply {
            value = (value + 2).toByte()
        }
    }

    override fun execute(instruction: Instruction) {
        // executes iff the lambda exists and is non-null
        if (digestMap[instruction.name] != null) {
            digestMap[instruction.name]?.let { it(instruction) }
        } else {
            logger.error("No digest exists for instruction ${instruction.name}")
        }
    }

    override val digestMap: Map<String, (Instruction) -> Unit> = mutableMapOf(
        "00E0" to { context.graphicsContext.clearScreen() }, // cls
        "00EE" to { // ret from subroutine
            withContext {
                instructionPointerRegisterBank.value = stackMemory[stackPointerRegisterBank.value.toInt()]
                stackPointerRegisterBank.value = (stackPointerRegisterBank.value - 1).toByte()
            }
        },
        "1NNN" to { // jump to `NNN` address
            withContext {
                instructionPointerRegisterBank.value = (it.args[0].toInt() and 0x0fff).toShort()
            }
        },
        "2NNN" to { // call `NNN` address
            withContext {
                stackPointerRegisterBank.value = (stackPointerRegisterBank.value + 1).toByte()
                stackMemory[stackPointerRegisterBank.value.toInt()] = instructionPointerRegisterBank.value
                instructionPointerRegisterBank.value = it.args[0]
            }
        },
        "3XKK" to { // SE Vx, skip next iff V[x] == kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] == kk.toByte()) {
                    skipNext()
                }
            }
        },
        "4XKK" to { // SNE Vx, skip next iff V[x] != kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] != kk.toByte()) {
                    skipNext()
                }
            }
        },
        "5XY0" to { // SE Vx, Vy, skip next iff V[x] == V[y]
            withContext {
                val x = it.args[0]
                val y = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] == generalPurposeRegisterBank[y.toInt()]) {
                    skipNext()
                }
            }
        },
        "6XKK" to { // LD Vx, load `kk` into V[x] register
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                generalPurposeRegisterBank[x.toInt()] = kk.toByte()
            }
        },
        "7XKK" to { // ADD Vx, => V[x] += kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                generalPurposeRegisterBank[x.toInt()] = (generalPurposeRegisterBank[x.toInt()] + kk).toByte()
            }
        },
        "8XY0" to { // LD XY, assigns the value of V[y] to V[x]
            withContext {
                updateXWithOperated(it) { _, y -> y }
            }
        },
        "8XY1" to { // X = X bitwise-or Y
            withContext {
                updateXWithOperated(it) { x, y -> x or y }
            }
        },
        "8XY2" to { // X = X bitwise-and Y
            withContext {
                updateXWithOperated(it) { x, y -> x and y }
            }
        },
        "8XY3" to { // X = X bitwise-xor Y
            withContext {
                updateXWithOperated(it) { x, y -> x xor y }
            }
        },
        "8XY4" to { // ADD XY, add V[x] and V[y] and set it on V[x]
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
        },
        "8XY5" to { // SUB XY, V[x] = V[x] - V[y], if V[x] > V[y], V[F] = 1
            withContext {
                updateXWithOperated(it) { x, y -> (x - y).toByte() }
                val x = generalPurposeRegisterBank[it.args[0].toInt()].toInt()
                val y = generalPurposeRegisterBank[it.args[1].toInt()].toInt()
                if (x > y) {
                    generalPurposeRegisterBank[0xf] = 1
                } else {
                    generalPurposeRegisterBank[0xf] = 0
                }
            }
        },
        "8XY6" to { // V[x] >>>= 1, V[y] is ignored
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()]
                generalPurposeRegisterBank[0xf] = xValue.leastSignificantBit
                updateXWithOperated(it) { x, _ -> (x.toInt() ushr 1).toByte() }
            }
        },
        "8XY7" to { // SUBN XY, V[x] = V[y] - V[x], if V[y] > V[x], V[F] = 1
            withContext {
                updateXWithOperated(it) { x, y -> (y - x).toByte() }
                val x = generalPurposeRegisterBank[it.args[0].toInt()].toInt()
                val y = generalPurposeRegisterBank[it.args[1].toInt()].toInt()
                if (y > x) {
                    generalPurposeRegisterBank[0xf] = 1
                } else {
                    generalPurposeRegisterBank[0xf] = 0
                }
            }
        },
        "8XYE" to { // V[x] <<= 1, set V[F] as most significant bit of V[x] before left-shift
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()]
                if (xValue.mostSignificantBit == 1.toByte()) {
                    generalPurposeRegisterBank[0xf] = 1
                }
                updateXWithOperated(it) { x, _ -> (x.toInt() shl 1).toByte() }
            }
        },
        "9XY0" to { // SNE XY, skip next if V[x] != V[y]
            withContext {
                val x = it.args[0].toInt()
                val y = it.args[1].toInt()
                if (generalPurposeRegisterBank[x] != generalPurposeRegisterBank[y]) {
                    skipNext()
                }
            }
        },
        "ANNN" to { // set IP as `NNN`
            withContext {
                instructionPointerRegisterBank.value = it.args[0]
            }
        },
        "BNNN" to { // jump to location `NNN` + V[0]
            withContext {
                instructionPointerRegisterBank.apply {
                    value = (it.args[0] + generalPurposeRegisterBank[0]).toShort()
                }
            }
        },
        "CXKK" to {
            withContext {
                val random = (0..255).random()
                val x = it.args[0].toInt()
                val kk = it.args[1].toInt()
                generalPurposeRegisterBank[x] = (random and kk).toByte()
            }
        },
        "FX07" to {
            withContext {
                val x = it.args[0].toInt()
                generalPurposeRegisterBank[x] = delayTimerRegisterBank.value
            }
        },
        "FX15" to {
            withContext {
                val x = it.args[0].toInt()
                delayTimerRegisterBank.value = generalPurposeRegisterBank[x]
            }
        },
        "FX18" to {
            withContext {
                val x = it.args[0].toInt()
                soundTimerRegisterBank.value = generalPurposeRegisterBank[x]
            }
        },
        "FX1E" to { // I += V[x]
            withContext {
                instructionPointerRegisterBank.apply {
                    val x = it.args[0].toInt()
                    val unsignedVx = generalPurposeRegisterBank[x].toUInt()
                    val unsignedIp = this.value.toUInt()
                    this.value = (unsignedIp + unsignedVx).toShort()
                }
            }
        },
        "FX29" to { // point IP to the hexadecimal sprite address of [x]
            withContext {
                val x = it.args[0].toInt()
                instructionPointerRegisterBank.value = graphicsContext.hexadecimalSpriteAddress(x)
            }
        },
        "FX33" to { // load V[x], convert to BCD, and store at [IP, IP + 1, IP + 2] as [H, T, O]
            withContext {
                val xValue = it.args[0].toUByte()
                instructionPointerRegisterBank.value.toInt().apply {
                    generalMemory[this] = xValue.hundreds.toByte()
                    generalMemory[this + 1] = xValue.tens.toByte()
                    generalMemory[this + 2] = xValue.ones.toByte()
                }
            }
        },
        "FX55" to { // store the registers into the memory at IP
            withContext {
                val x = it.args[0]
                for (i in 0..x) {
                    generalMemory[instructionPointerRegisterBank.value + i] = generalPurposeRegisterBank[i]
                }
            }
        },
        "FX65" to { // load the registers from memory at IP
            withContext {
                val x = it.args[0]
                for (i in 0..x) {
                    generalPurposeRegisterBank[i] = generalMemory[instructionPointerRegisterBank.value + i]
                }
            }
        },
        "DXYN" to {

        },
        "EX9E" to {

        },
        "EXA1" to {

        },
        "FX0A" to {

        }
    )
}