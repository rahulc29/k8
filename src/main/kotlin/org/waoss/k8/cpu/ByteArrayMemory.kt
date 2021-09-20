package org.waoss.k8.cpu

class ByteArrayMemory(override val size: Int = 0, private val array: ByteArray = ByteArray(size)) : Memory<Byte> {

    override operator fun get(index: Int): Byte = array[index]

    override operator fun set(index: Int, value: Byte) {
        array[index] = value
    }

    override fun forEach(digest: (Byte) -> Unit) {
        array.forEach(digest)
    }
}