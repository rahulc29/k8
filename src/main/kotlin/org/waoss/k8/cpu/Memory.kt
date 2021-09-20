package org.waoss.k8.cpu

import org.waoss.k8.Loggable

interface Memory<T : Number> : Loggable {
    val size: Int
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T)
    fun forEach(digest: (T) -> Unit)
}

fun IOEngine.constructMemory(): Memory<Byte> {
    val read = this.readAll()
    return ByteArrayMemory(size = read.size, array = read)
}