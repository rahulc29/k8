package org.waoss.k8.jfx

import javafx.scene.canvas.Canvas
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.gpu.Position
import java.util.*

fun ByteArray.clear() {
    Arrays.fill(this, 0)
}

class FXGraphicsContext : GraphicsContext, Canvas(64.0, 32.0) {

    private val frameBuffer = Array(32) { ByteArray(64) }
    override fun clearScreen() {
        frameBuffer.forEach(ByteArray::clear)
    }

    override fun hexadecimalSpriteAddress(x: Int): Short {
        TODO("Not yet implemented")
    }

    override fun draw(position: Position<Int>, value: Byte) {
        val x = position.x
        val y = position.y
        frameBuffer[x][y] = value
    }
}