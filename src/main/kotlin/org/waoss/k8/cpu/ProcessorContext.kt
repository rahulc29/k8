package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.input.Keyboard

interface ProcessorContext : Loggable {
    val graphicsContext: GraphicsContext
    val generalPurposeRegisterBank: GeneralPurposeRegisterBank
    val stackPointer: StackPointerRegisterBank
    val instructionPointer: InstructionPointerRegisterBank
    val iRegister: IRegisterBank
    val soundTimer: SoundTimerRegisterBank
    val delayTimer: DelayTimerRegisterBank
    var generalMemory: Memory<Byte>
    val stackMemory: Memory<Short>
    val keyboard: Keyboard
}

internal class ProcessorContextImpl(override val graphicsContext: GraphicsContext, override val keyboard: Keyboard) :
    ProcessorContext {
    override val generalPurposeRegisterBank: GeneralPurposeRegisterBank = GeneralPurposeRegisterBank()
    override val stackPointer: StackPointerRegisterBank = StackPointerRegisterBank(0)
    override val instructionPointer: InstructionPointerRegisterBank = InstructionPointerRegisterBank(0x0200)
    override val iRegister: IRegisterBank = IRegisterBank(0)
    override val soundTimer: SoundTimerRegisterBank = SoundTimerRegisterBank(0)
    override val delayTimer: DelayTimerRegisterBank = DelayTimerRegisterBank(0)
    override var generalMemory: Memory<Byte> = ByteArrayMemory(size = 4096, array = ByteArray(size = 4096))
    override val stackMemory: Memory<Short> = ShortArrayMemory(size = 16)
}

fun defaultProcessorContext(graphicsContext: GraphicsContext, keyboard: Keyboard): ProcessorContext =
    ProcessorContextImpl(graphicsContext, keyboard)