package org.waoss.k8.io

import kotlinx.coroutines.Deferred

interface AsyncIOEngine {
    suspend fun readAllAsync(): Deferred<ByteArray>
}