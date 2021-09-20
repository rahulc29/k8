package org.waoss.k8.cpu

class ShortArrayMemory(override val size: Int = 0, private val array: ShortArray = ShortArray(size)) : Memory<Short> {

    override operator fun get(index: Int): Short = array[index]

    override operator fun set(index: Int, value: Short) {
        array[index] = value
    }

    override fun forEach(digest: (Short) -> Unit) {
        array.forEach(digest)
    }
}