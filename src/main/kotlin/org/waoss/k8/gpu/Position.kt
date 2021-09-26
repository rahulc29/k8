package org.waoss.k8.gpu

open class Position<T : Number>(open val x: T, open val y: T)

internal class AlgebraicPosition(x: Int, y: Int) : Position<Int>(x, y) {
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

fun positionOf(x: Int, y: Int): Position<Int> = AlgebraicPosition(x = x, y = y)