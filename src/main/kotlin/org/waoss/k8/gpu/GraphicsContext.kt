package org.waoss.k8.gpu

import org.waoss.k8.Loggable

interface GraphicsContext : Loggable {
    fun clearScreen()
    fun hexadecimalSpriteAddress(x: Int): Short
    fun draw(position: Position<Int>, value: Byte)
}