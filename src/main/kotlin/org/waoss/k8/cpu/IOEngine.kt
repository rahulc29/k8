package org.waoss.k8.cpu

import org.waoss.k8.Loggable

interface IOEngine : Loggable {
    fun readAll() : ByteArray
}