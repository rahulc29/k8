package org.waoss.k8

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
) {
    suspend fun executionLoop() {
        val deferredByteArray = ioEngine.readAllAsync()
        val memory = ByteArrayMemory(deferredByteArray.await())
        val instructionPointer = processorContext.instructionPointer
        while (instructionPointer.value <= 4096) {
            val bytes = memory[instructionPointer.value.toInt()] to memory[instructionPointer.value.toInt()]
            val instruction = parsingEngine.parse(bytes)
            if (!executionEngine.execute(instruction)) {
                instructionPointer.inc()
            }
            graphicsContext.render()
        }
    }
}