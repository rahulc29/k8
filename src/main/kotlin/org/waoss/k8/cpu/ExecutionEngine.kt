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
                    stackPointerRegisterBank.apply {
                        value = (value + 2).toByte()
                    }
                }
            }
        },
        "4XKK" to { // SNE Vx, skip next iff V[x] != kk
            withContext {
                val x = it.args[0]
                val kk = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] != kk.toByte()) {
                    stackPointerRegisterBank.apply {
                        value = (value + 2).toByte()
                    }
                }
            }
        },
        "5XY0" to { // SE Vx, Vy, skip next iff V[x] == V[y]
            withContext {
                val x = it.args[0]
                val y = it.args[1]
                if (generalPurposeRegisterBank[x.toInt()] == generalPurposeRegisterBank[y.toInt()]) {
                    stackPointerRegisterBank.apply {
                        value = (value + 2).toByte()
                    }
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
        "8XY6" to {
            withContext {
                val xValue = generalPurposeRegisterBank[it.args[0].toInt()]
                generalPurposeRegisterBank[0xf] = xValue.leastSignificantBit
                updateXWithOperated(it) { x, _ -> (x.toInt() ushr 1).toByte()}
            }
        },
        "8XY7" to {

        }
    )
}