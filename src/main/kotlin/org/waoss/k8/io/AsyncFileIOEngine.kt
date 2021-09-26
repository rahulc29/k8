package org.waoss.k8.io

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.nio.file.Files
import java.nio.file.Paths

class AsyncFileIOEngine(private val name: String) : AsyncIOEngine {
    override suspend fun readAllAsync(): Deferred<ByteArray> {
        return coroutineScope {
            async {
                Files.readAllBytes(Paths.get(name))
            }
        }
    }
}