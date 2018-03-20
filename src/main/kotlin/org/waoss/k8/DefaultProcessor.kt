package org.waoss.k8

class DefaultProcessor : Processor {
    override val memory: ByteArray = ByteArray(size = 4096)
    override val registers: Map<String, RegisterValue> = mapOf(
            "0" to RegisterValue(),
            "1" to RegisterValue(),
            "2" to RegisterValue(),
            "3" to RegisterValue(),
            "4" to RegisterValue(),
            "5" to RegisterValue(),
            "6" to RegisterValue(),
            "7" to RegisterValue(),
            "8" to RegisterValue(),
            "9" to RegisterValue(),
            "a" to RegisterValue(),
            "b" to RegisterValue(),
            "c" to RegisterValue(),
            "d" to RegisterValue(),
            "e" to RegisterValue(),
            "f" to RegisterValue(),
            "i" to RegisterValue(),
            "dt" to RegisterValue(),
            "st" to RegisterValue(),
            "sp" to RegisterValue(),
            "pc" to RegisterValue(short = 0x200)
    )
    override val registerTypes: Map<String, RegisterType> = mapOf(
            "0" to RegisterType.BYTE,
            "1" to RegisterType.BYTE,
            "2" to RegisterType.BYTE,
            "3" to RegisterType.BYTE,
            "4" to RegisterType.BYTE,
            "5" to RegisterType.BYTE,
            "6" to RegisterType.BYTE,
            "7" to RegisterType.BYTE,
            "8" to RegisterType.BYTE,
            "9" to RegisterType.BYTE,
            "a" to RegisterType.BYTE,
            "b" to RegisterType.BYTE,
            "c" to RegisterType.BYTE,
            "d" to RegisterType.BYTE,
            "e" to RegisterType.BYTE,
            "f" to RegisterType.BYTE,
            "i" to RegisterType.BYTE,
            "dt" to RegisterType.BYTE,
            "st" to RegisterType.BYTE,
            "sp" to RegisterType.BYTE,
            "pc" to RegisterType.SHORT
    )
    override val instructionExecutions: Map<String, () -> Unit> = mapOf()

    override fun run() {

    }
}