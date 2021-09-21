package org.waoss.k8.gpu

interface GraphicsContext {
    fun clearScreen()
    fun hexadecimalSpriteAddress(x: Int): Short
}
