package org.waoss.k8.cpu

import org.waoss.k8.Loggable
import org.waoss.k8.logger
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed interface RegisterBank<T : Number> : Loggable {
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T)
}

fun <T : Number> T.toHexString(): String = this.toInt().toString(radix = 16)
fun <T : Number> T.toUHexString(): String = this.toByte().toUByte().toString(radix = 16)

fun Loggable.logSetterInvoked(name: String, value: String) {
    logger.info("Value of $name changed to $value")
}

fun <T : Number> singletonRegisterBankDelegate(
    name: String,
    initial: T
): ReadWriteProperty<SingletonRegisterBank<T>, T> =
    object : ReadWriteProperty<SingletonRegisterBank<T>, T>, Loggable {
        var value: T = initial
        override fun getValue(thisRef: SingletonRegisterBank<T>, property: KProperty<*>): T {
            return value
        }

        override fun setValue(thisRef: SingletonRegisterBank<T>, property: KProperty<*>, value: T) {
            logSetterInvoked(name, value.toHexString())
            this.value = value
        }

    }

sealed class ByteRegisterBank : RegisterBank<Byte>

sealed class SingletonRegisterBank<T : Number>(open var value: T) : RegisterBank<T> {
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

    private fun throwIndexOutOfBounds(index: Int): Nothing {
        throw IndexOutOfBoundsException("Index $index cannot possibly exist for singleton register bank")
    }
}

class GeneralPurposeRegisterBank : ByteRegisterBank() {
    private val array = ByteArray(size = 16)
    override fun get(index: Int): Byte {
        return array[index]
    }

    override fun set(index: Int, value: Byte) {
        logger.info("General purpose register V[$index] changed to ${value.toUHexString()}")
        array[index] = value
    }

    fun forEach(digest: (Byte) -> Unit) {
        array.forEach(digest)
    }
}

class InstructionPointerRegisterBank(value: Short) : SingletonRegisterBank<Short>(value) {
    override var value: Short by singletonRegisterBankDelegate("Instruction Pointer", value)
    operator fun inc(): InstructionPointerRegisterBank {
        this.value = (value + 2).toShort()
        return this
    }
}

class StackPointerRegisterBank(value: Byte) : SingletonRegisterBank<Byte>(value) {
    override var value: Byte by singletonRegisterBankDelegate("Stack Pointer", value)
}

class DelayTimerRegisterBank(value: Byte) : SingletonRegisterBank<Byte>(value) {
    override var value: Byte by singletonRegisterBankDelegate("Delay Timer", value)
}

class SoundTimerRegisterBank(value: Byte) : SingletonRegisterBank<Byte>(value) {
    override var value: Byte by singletonRegisterBankDelegate("Sound Timer", value)
}

class IRegisterBank(value: Short) : SingletonRegisterBank<Short>(value) {
    override var value: Short by singletonRegisterBankDelegate("I", value)
}