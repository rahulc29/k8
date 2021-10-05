package org.waoss.k8.javafx

import javafx.application.Platform
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import org.waoss.k8.gpu.GraphicsContext
import org.waoss.k8.gpu.Position

typealias PlatformGraphicsContext = javafx.scene.canvas.GraphicsContext

const val SCALE = 12.0

inline val Int.scaled: Double
    get() = this * SCALE

class FXGraphicsContext(width: Int, height: Int) : Canvas(width.scaled, height.scaled), GraphicsContext {

    private val graphicsContext: PlatformGraphicsContext = graphicsContext2D
    private val frameBuffer = Array(width) { BooleanArray(height) }

    init {
        isFocusTraversable = true
        graphicsContext.apply {
            fill = Color.BLACK
            fillRect(0.0, 0.0, width.scaled, height.scaled)
        }
    }

    override fun clearScreen() {
        frameBuffer.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ ->
                frameBuffer[i][j] = false
            }
        }
    }

    override fun hexadecimalSpriteAddress(x: Int): Short {
        return (0x00 + x * 5).toShort()
    }

    override fun draw(position: Position, value: Byte) {
        frameBuffer[position.x][position.y] = value == 1.toByte()
        render()
    }

    override fun draw(x: Int, y: Int) {
        frameBuffer[x][y] = frameBuffer[x][y] xor true
        render()
    }

    override fun get(index1: Int, index2: Int): Boolean = frameBuffer[index1][index2]

    override fun get(position: Position): Boolean = get(position.x, position.y)

    override fun render() {
        Platform.runLater {
            graphicsContext.apply {
                frameBuffer.forEachIndexed { i, row ->
                    row.forEachIndexed { j, value ->
                        fill = if (value) {
                            Color.WHITE
                        } else {
                            Color.BLACK
                        }
                        fillRect(i.scaled, j.scaled, SCALE, SCALE)
                    }
                }
            }
        }
    }

}
