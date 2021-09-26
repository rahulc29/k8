package org.waoss.k8.io

import java.nio.file.Files
import java.nio.file.Paths

class SynchronousFileIOEngine(private val name: String) : IOEngine {
    override suspend fun readAll(): ByteArray = lazy {
        // synchronous call
        // use with caution
        Paths.get(name).let { Files.readAllBytes(it) }
    }.value
}