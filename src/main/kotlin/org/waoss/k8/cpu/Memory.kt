package org.waoss.k8.cpu

interface Memory {
    val size: Int
    operator fun get(index: Int): Byte
    operator fun set(index: Int, value: Byte)
    fun forEach(digest: (Byte) -> Unit)
}

fun IOEngine.constructMemory(): Memory {
    val read = this.readAll()
    return ByteArrayMemory(size = read.size, array = read)
}