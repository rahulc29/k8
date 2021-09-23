package org.waoss.k8.gpu

interface GraphicsContext {
    fun clearScreen()
    fun hexadecimalSpriteAddress(x: Int): Short
    fun draw(position: Position<Int>, value: Byte)
}

interface Position<T : Number> {
    val x: T
    val y: T
}

class AlgebraicPosition(x: Int, y: Int) : Position<Int> {
    override val x: Int = x
        get() {
            return field % 64
        }
    override val y: Int = y
        get() {
            return field % 32
        }

    override fun toString(): String {
        return "(${this.x}, ${this.y})"
    }
}

fun positionOf(x: Int, y: Int) = AlgebraicPosition(x = x, y = y)