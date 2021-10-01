package org.waoss.k8.io

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File

class FileIOEngine(private val file: File) : IOEngine {
    override suspend fun readAllAsync(): Deferred<ByteArray> = coroutineScope {
        async {
            val stream = file.inputStream()
            val array = ByteArray(file.length().toInt())
            stream.read(array)
            return@async array
        }
    }
}