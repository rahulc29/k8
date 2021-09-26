package org.waoss.k8.io

import org.waoss.k8.Loggable

interface IOEngine : Loggable {
    fun readAll() : ByteArray
}