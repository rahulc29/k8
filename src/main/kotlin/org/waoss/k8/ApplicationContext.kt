package org.waoss.k8

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import org.waoss.k8.cpu.ByteArrayMemory
import org.waoss.k8.cpu.ExecutionEngine
import org.waoss.k8.cpu.ParsingEngine
import org.waoss.k8.cpu.ProcessorContext
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.io.IOEngine

open class ApplicationContext(
    protected val graphicsContext: GraphicsContext,
    protected val processorContext: ProcessorContext,
    protected val executionEngine: ExecutionEngine,
    protected val ioEngine: IOEngine,
    protected val parsingEngine: ParsingEngine
) : Loggable {
    suspend fun executionLoop(coroutineScope: CoroutineScope) {
        val deferredByteArray = ioEngine.readAllAsync()
        val memory = ByteArrayMemory(array = deferredByteArray.await())
        processorContext.generalMemory = memory
        val instructionPointer = processorContext.instructionPointer
        while (instructionPointer.value < 4096 && coroutineScope.isActive) {
            val address = instructionPointer.value.toInt()
            val bytes = memory[address] to memory[address + 1]
            val instruction = parsingEngine.parse(bytes)
            if (!executionEngine.execute(instruction)) {
                instructionPointer.inc()
            }
        }
    }
}