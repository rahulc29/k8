package org.waoss.k8.jfx

import javafx.collections.FXCollections.observableArrayList
import javafx.collections.ObservableList
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.gpu.Position

typealias PlatformGraphicsContext = javafx.scene.canvas.GraphicsContext

fun ObservableList<Boolean>.allToFalse() {
    for (i in 0 until this.size) {
        this[i] = false
    }
}

class FXGraphicsContext : GraphicsContext, Canvas(800.0, 400.0) {
    companion object {
        private const val SCALE = 12.0
    }

    private val platformGraphicsContext = this.graphicsContext2D
    private val frameBuffer: ObservableList<ObservableList<Boolean>> =
        observableArrayList()

    init {
        isFocusTraversable = true
        platformGraphicsContext.apply {
            fill = Color.BLACK
            fillRect(0.0, 0.0, 800.0, 400.0)
        }
    }

    private fun PlatformGraphicsContext.render() {
        for (i in frameBuffer.indices) {
            for (j in frameBuffer[i].indices) {
                fill = if (frameBuffer[i][j]) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
                fillRect(i * SCALE, j * SCALE, SCALE, SCALE)
            }
        }
    }

    fun render() {
        platformGraphicsContext.render()
    }

    override fun clearScreen() {
        frameBuffer.forEach(ObservableList<Boolean>::allToFalse)
        platformGraphicsContext.render()
    }

    override fun hexadecimalSpriteAddress(x: Int): Short {
        TODO("Not yet implemented")
    }

    override fun draw(position: Position<Int>, value: Byte) {
        val x = position.x
        val y = position.y
        frameBuffer[x][y] = (value == 1.toByte())
    }
}