package org.waoss.k8.io

import kotlinx.coroutines.Deferred

interface AsyncIOEngine : IOEngine {
    suspend fun readAllAsync(): Deferred<ByteArray>

    /**
     * Synchronous overload of reading it all
     */
    override suspend fun readAll(): ByteArray {
        return readAllAsync().await()
    }
}