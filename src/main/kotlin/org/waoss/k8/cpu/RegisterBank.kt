package org.waoss.k8.cpu

sealed interface RegisterBank<T : Number> {
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T)
}

sealed class ByteRegisterBank : RegisterBank<Byte>

sealed class SingletonRegisterBank<T : Number>(var value: T) : RegisterBank<T> {
    override fun get(index: Int): T {
        if (index != 0) {
            throwIndexOutOfBounds(index)
        }
        return value
    }

    override fun set(index: Int, value: T) {
        if (index != 0) {
            throwIndexOutOfBounds(index)
        }
        this.value = value
    }

    protected fun throwIndexOutOfBounds(index: Int) : Nothing {
        throw IndexOutOfBoundsException("Index $index cannot possibly exist for lone register bank")
    }
}

class GeneralPurposeRegisterBank : ByteRegisterBank() {
    private val array = ByteArray(16)
    override fun get(index: Int): Byte = array[index]
    override fun set(index: Int, value: Byte) {
        array[index] = value
    }
}

class InstructionPointerRegisterBank(value: Short) : SingletonRegisterBank<Short>(value)
class StackPointerRegisterBank(value: Byte) : SingletonRegisterBank<Byte>(value)