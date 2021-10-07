package org.waoss.k8.cpu

class ByteArrayMemory(override val size: Int, val array: ByteArray) : Memory<Byte> {

    constructor(array: ByteArray, startValue: Int = 0x200) : this(size = array.size, array = ByteArray(4096)) {
        array.copyInto(this.array, destinationOffset = startValue, startIndex = 0)
    }

    override operator fun get(index: Int): Byte = array[index]

    override operator fun set(index: Int, value: Byte) {
        array[index] = value
    }

    override fun forEach(digest: (Byte) -> Unit) {
        array.forEach(digest)
    }
}