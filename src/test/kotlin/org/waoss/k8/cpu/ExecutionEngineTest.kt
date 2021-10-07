package org.waoss.k8.cpu

import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.gpu.Position
import org.waoss.k8.input.Key
import org.waoss.k8.input.Keyboard
import org.waoss.k8.input.key
import org.waoss.k8.logger
import kotlin.test.Test

val processorContext = object : ProcessorContext {
    override val graphicsContext: GraphicsContext = object : GraphicsContext {
        override fun clearScreen() {
            logger.info("Screen has been cleared")
        }

        override fun hexadecimalSpriteAddress(x: Int): Short {
            return 0x69
        }

        override fun draw(position: Position, value: Byte) {
            logger.info("Drawing $value at $position")
        }

        override fun get(index1: Int, index2: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun get(position: Position): Boolean {
            TODO("Not yet implemented")
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

internal class ExecutionEngineTest {

    @Test
    fun execute() {

        val executionEngine = ExecutionEngineImpl(processorContext)
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
            constructInstruction("8XY0", 3, 2),
            constructInstruction("8XY1", 2, 1),
            constructInstruction("8XY2", 2, 1),
            constructInstruction("8XY3", 2, 1),
            constructInstruction("8XY4", 2, 1),
            constructInstruction("8XY5", 2, 1),
            constructInstruction("8XY6", 2, 1),
            constructInstruction("8XY7", 2, 1),
            constructInstruction("8XYE", 2, 1)
        )
        instructionList.forEach { println(executionEngine.execute(it)) }
    }
}