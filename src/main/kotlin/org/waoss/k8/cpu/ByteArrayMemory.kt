package org.waoss.k8.cpu

class ByteArrayMemory(override val size: Int = 0, val array: ByteArray = ByteArray(size)) : Memory<Byte> {

    constructor(array: ByteArray, startValue: Int = 0x200) : this(size = array.size, array = ByteArray(4096)) {
        val readBytesSize = array.size
        this.array.let {
            for (i in 0 until readBytesSize) {
                it[startValue + i] = array[i]
            }
        }
    }

    override operator fun get(index: Int): Byte = array[index]

    override operator fun set(index: Int, value: Byte) {
        array[index] = value
    }

    override fun forEach(digest: (Byte) -> Unit) {
        array.forEach(digest)
    }
}