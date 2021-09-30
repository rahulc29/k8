package org.waoss.k8.gpu

import org.waoss.k8.Loggable

interface GraphicsContext : Loggable {
    fun clearScreen()
    fun hexadecimalSpriteAddress(x: Int): Short
    fun draw(position: Position, value: Byte)
    operator fun get(index1: Int, index2: Int): Boolean
    operator fun get(position: Position): Boolean
}