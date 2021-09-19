package org.waoss.k8.cpu

interface ExecutionEngine {
    fun execute(instruction: Instruction)
    val lambdaMap: Map<String, (Instruction) -> Unit>
    val context: Context
}

internal class ExecutionEngineImpl(override val context: Context) : ExecutionEngine {
    override fun execute(instruction: Instruction) {
        // executes iff the lambda exists and is non-null
        lambdaMap[instruction.name]?.let { it(instruction) }
    }

    override val lambdaMap: Map<String, (Instruction) -> Unit> = mutableMapOf(
        "00E0" to { context.graphicsContext.clearScreen() }

    )
}