package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.gpu.GraphicsContext

interface Context : Loggable {
    val graphicsContext: GraphicsContext
    val generalPurposeRegisterBank: GeneralPurposeRegisterBank
    val stackPointerRegisterBank: StackPointerRegisterBank
    val instructionPointerRegisterBank: InstructionPointerRegisterBank
    val generalMemory: Memory<Byte>
    val stackMemory: Memory<Short>
}