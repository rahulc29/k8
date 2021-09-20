package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.logger

sealed interface RegisterBank<T : Number> : Loggable {
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
        throw IndexOutOfBoundsException("Index $index cannot possibly exist for singleton register bank")
    }
}

class GeneralPurposeRegisterBank : ByteRegisterBank() {
    private val array = ByteArray(size = 16)
    override fun get(index: Int): Byte {
        return array[index]
    }
    override fun set(index: Int, value: Byte) {
        logger.info("General purpose register V[$index] changed to ${value.toString(radix = 16)}")
        array[index] = value
    }
}

class InstructionPointerRegisterBank(value: Short) : SingletonRegisterBank<Short>(value) {
    override fun set(index: Int, value: Short) {
        super.set(index, value)
        logger.info("Instruction pointer changed to ${value.toString(radix = 16)}")
    }
}
class StackPointerRegisterBank(value: Byte) : SingletonRegisterBank<Byte>(value) {
    override fun set(index: Int, value: Byte) {
        super.set(index, value)
        logger.info("Stack pointer changed to ${value.toString(radix = 16)}")
    }
}