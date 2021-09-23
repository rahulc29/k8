package org.waoss.k8.cpu

import org.junit.jupiter.api.Test
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.gpu.Position
import org.waoss.k8.input.Key
import org.waoss.k8.input.Keyboard
import org.waoss.k8.input.key
import org.waoss.k8.logger

internal class ExecutionEngineTest {

    @Test
    fun execute() {
        val context = object : Context {
            override val graphicsContext: GraphicsContext = object : GraphicsContext {
                override fun clearScreen() {
                    logger.info("Screen has been cleared")
                }

                override fun hexadecimalSpriteAddress(x: Int): Short {
                    return 0x69
                }

                override fun draw(position: Position<Int>, value: Byte) {
                    logger.info("Drawing $value at $position")
                }

            }
            override val generalPurposeRegisterBank: GeneralPurposeRegisterBank = GeneralPurposeRegisterBank()
            override val stackPointer: StackPointerRegisterBank = StackPointerRegisterBank(0)
            override val instructionPointer: InstructionPointerRegisterBank = InstructionPointerRegisterBank(0x0200)
            override val soundTimer: SoundTimerRegisterBank = SoundTimerRegisterBank(0)
            override val delayTimer: DelayTimerRegisterBank = DelayTimerRegisterBank(0)
            override val generalMemory: Memory<Byte> = ByteArrayMemory(size = 4096)
            override val stackMemory: Memory<Short> = ShortArrayMemory(size = 16)
            override val keyboard: Keyboard = object : Keyboard {
                override fun isPressed(key: Key): Boolean {
                    return (0..1).random() == 1
                }

                override fun nextKey(): Key {
                    return (0..16).random().toByte().key
                }
            }
        }
        val executionEngine = ExecutionEngineImpl(context)
        val instructionList = listOf(
            constructInstruction("00E0"),
            constructInstruction("00EE"),
            constructInstruction("1NNN", 0xaaa.toShort()),
            constructInstruction("2NNN", 0xaaa.toShort()),
            constructInstruction("3XKK", 3, 0),
            constructInstruction("4XKK", 3, 5),
            constructInstruction("5XY0", 1, 3),
            constructInstruction("6XKK", 2, 0xea),
            constructInstruction("7XKK", 2, 0xe),
            constructInstruction("8XY0", 3, 2)
        )
        instructionList.forEach(executionEngine::execute)
    }
}