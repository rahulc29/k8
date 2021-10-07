package org.waoss.k8.gpu

abstract class Position(open val x: Int, open val y: Int)

internal class AlgebraicPosition(x: Int, y: Int) : Position(x, y) {
    override val x: Int = x % 64
    override val y: Int = y % 32
    override fun toString(): String {
        return "(${this.x}, ${this.y})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlgebraicPosition

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

fun positionOf(x: Int, y: Int): Position = AlgebraicPosition(x = x, y = y)