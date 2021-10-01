package org.waoss.k8.io

import kotlinx.coroutines.Deferred

interface IOEngine {
    suspend fun readAllAsync(): Deferred<ByteArray>
}