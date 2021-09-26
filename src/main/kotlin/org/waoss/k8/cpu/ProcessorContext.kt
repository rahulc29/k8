package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.input.Keyboard

interface ProcessorContext : Loggable {
    val graphicsContext: GraphicsContext
    val generalPurposeRegisterBank: GeneralPurposeRegisterBank
    val stackPointer: StackPointerRegisterBank
    val instructionPointer: InstructionPointerRegisterBank
    val soundTimer: SoundTimerRegisterBank
    val delayTimer: DelayTimerRegisterBank
    val generalMemory: Memory<Byte>
    val stackMemory: Memory<Short>
    val keyboard: Keyboard
}