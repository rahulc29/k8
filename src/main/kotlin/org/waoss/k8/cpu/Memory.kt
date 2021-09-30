package org.waoss.k8.cpu

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.waoss.k8.Loggable
import org.waoss.k8.io.AsyncIOEngine

interface Memory<T : Number> : Loggable {
    val size: Int
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T)
    fun forEach(digest: (T) -> Unit)
}

suspend fun AsyncIOEngine.constructMemory(): Deferred<Memory<Byte>> = coroutineScope {
    async {
        val array = readAllAsync().await() // blocking call but safe in async {}
        return@async ByteArrayMemory(array = array, size = array.size)
    }
}