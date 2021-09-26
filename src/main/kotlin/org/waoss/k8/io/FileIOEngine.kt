package org.waoss.k8.io

import java.nio.file.Files
import java.nio.file.Paths

class FileIOEngine(private val name: String) : IOEngine {
    override fun readAll(): ByteArray {
        return Paths.get(name).let { Files.readAllBytes(it) }
    }
}