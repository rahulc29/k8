package org.waoss.k8.cpu

interface ExecutionEngine {
    fun execute(instruction: Instruction)
    val lambdaMap: Map<String, (Instruction) -> Unit>
}