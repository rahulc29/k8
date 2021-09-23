package org.waoss.k8.input

import org.waoss.k8.input.Key.*

interface Keyboard {
    fun isPressed(key: Key): Boolean
    fun nextKey(): Key
}

enum class Key {
    NUM1,
    NUM2,
    NUM3,
    NUM4,
    NUM5,
    NUM6,
    NUM7,
    NUM8,
    NUM9,
    NUM0,
    A,
    B,
    C,
    D,
    E,
    F
}

val Byte.key: Key
    get() {
        return when (this) {
            1.toByte() -> NUM1
            2.toByte() -> NUM2
            3.toByte() -> NUM3
            4.toByte() -> NUM4
            5.toByte() -> NUM5
            6.toByte() -> NUM6
            7.toByte() -> NUM7
            8.toByte() -> NUM8
            9.toByte() -> NUM9
            0.toByte() -> NUM0
            0xa.toByte() -> A
            0xb.toByte() -> B
            0xc.toByte() -> C
            0xd.toByte() -> D
            0xe.toByte() -> E
            0xf.toByte() -> F
            else -> throw IllegalStateException("No key exists for $this")
        }
    }

val Key.byte: Byte
    get() {
        return when (this) {
            NUM1 -> 1.toByte()
            NUM2 -> 2.toByte()
            NUM3 -> 3.toByte()
            NUM4 -> 4.toByte()
            NUM5 -> 5.toByte()
            NUM6 -> 6.toByte()
            NUM7 -> 7.toByte()
            NUM8 -> 8.toByte()
            NUM9 -> 9.toByte()
            NUM0 -> 0.toByte()
            A -> 0xa.toByte()
            B -> 0xb.toByte()
            C -> 0xc.toByte()
            D -> 0xd.toByte()
            E -> 0xe.toByte()
            F -> 0xf.toByte()
        }
    }