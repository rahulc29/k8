package org.waoss.k8

interface Processor : Runnable {
    val memory: ByteArray
    val registers: Map<String, RegisterValue>
    val registerTypes: Map<String, RegisterType>
    val instructionExecutions: Map<String, () -> Unit>
}