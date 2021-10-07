package org.waoss.k8.cpu

import org.waoss.k8.Loggable

interface ParsingEngine : Loggable {
    fun parse(bytes: Pair<Byte, Byte>): Instruction
    fun parse(bytes: ByteArray, digest: (Instruction) -> Unit)
}

val Pair<Byte, Byte>.mostSignificant
    get() = this.first

val Pair<Byte, Byte>.leastSignificant
    get() = this.second

val UByte.hundreds: UByte
    get() = (this / 100.toUByte()).toUByte()

val UByte.tens: UByte
    get() = (this / 10.toUByte()).mod(10.toUByte())

val UByte.ones: UByte
    get() = (this.mod(10.toUByte()))

val Byte.leastSignificantBit: Byte
    get() = this and 1.toByte()

val Byte.mostSignificantBit: Byte
    get() = if ((this and 0x80.toByte()) == 0x80.toByte()) 1 else 0

// 0x00
val Short.x: Byte
    get() = ((this.toInt() and 0x0f00) ushr 8).toByte()

// 00y0
val Short.y: Byte
    get() = ((this.toInt() and 0x00f0) ushr 4).toByte()

// 00kk
val Short.leastSignificantByte: Byte
    get() = (this.toInt() and 0x00ff).toByte()

// kk00
val Short.mostSignificantByte: Byte
    get() = ((this.toInt() and 0xff00) ushr 8).toByte()

// 0nnn
val Short.nnn: Short
    get() = (this.toInt() and 0x0fff).toShort()

// a000
val Short.mostSignificantNibble: Byte
    get() = ((this.toInt() and 0xf000) ushr 12).toByte()

// 000b
val Short.leastSignificantNibble: Byte
    get() = (this.toInt() and 0x000f).toByte()

operator fun Short.get(index: Int): Byte {
    return when (index) {
        0 -> {
            this.mostSignificantNibble
        }
        1 -> {
            this.x
        }
        2 -> {
            this.y
        }
        else -> this.leastSignificantNibble
    }
}

infix fun Byte.and(that: Byte): Byte = (this.toInt() and that.toInt()).toByte()

infix fun Byte.or(that: Byte): Byte = (this.toInt() or that.toInt()).toByte()

infix fun Byte.xor(that: Byte): Byte = (this.toInt() xor that.toInt()).toByte()

infix fun Byte.shr(that: Byte): Byte = (this.toInt() shr that.toInt()).toByte()

internal object ParsingEngineImpl : ParsingEngine {

    private fun Pair<Byte, Byte>.toShort(): Short =
        ((mostSignificant.toInt() shl 8) or (leastSignificant.toInt() and 0x00ff)).toShort()

    private fun Short.xyInstruction(index: Int): Instruction {
        return constructInstruction(
            "8XY${index}",
            this.x.toShort(),
            this.y.toShort()
        )
    }

    override fun parse(bytes: Pair<Byte, Byte>): Instruction {
        val instructionRepresentation = bytes.toShort()
        with(instructionRepresentation) {
            if (this == 0x00e0.toShort()) {
                return constructInstruction("00E0")
            } else if (this == 0x00ee.toShort()) {
                return constructInstruction("00EE")
            } else if (this[0] == 1.toByte()) {
                return constructInstruction("1NNN", this.nnn)
            } else if (this[0] == 2.toByte()) {
                return constructInstruction("2NNN", this.nnn)
            } else if (this[0] == 3.toByte()) {
                return constructInstruction(
                    "3XKK",
                    this.x.toShort(),
                    this.leastSignificantByte.toShort()
                )
            } else if (this[0] == 4.toByte()) {
                return constructInstruction(
                    "4XKK",
                    this.x.toShort(),
                    this.leastSignificantByte.toShort()
                )
            } else if (this[0] == 5.toByte()) {
                return constructInstruction(
                    "5XY0",
                    this.x.toShort(),
                    this.y.toShort()
                )
            } else if (this[0] == 6.toByte()) {
                return constructInstruction(
                    "6XKK",
                    this.x.toShort(),
                    this.leastSignificantByte.toShort()
                )
            } else if (this[0] == 7.toByte()) {
                return constructInstruction(
                    "7XKK",
                    this.x.toShort(),
                    this.leastSignificantByte.toShort()
                )
            } else if (this[0] == 8.toByte()) {
                return when (this.leastSignificantNibble) {
                    0.toByte() -> this.xyInstruction(0)
                    1.toByte() -> this.xyInstruction(1)
                    2.toByte() -> this.xyInstruction(2)
                    3.toByte() -> this.xyInstruction(3)
                    4.toByte() -> this.xyInstruction(4)
                    5.toByte() -> this.xyInstruction(5)
                    6.toByte() -> this.xyInstruction(6)
                    7.toByte() -> this.xyInstruction(7)
                    0xe.toByte() -> constructInstruction(
                        "8XYE",
                        x.toShort(),
                        y.toShort()
                    )
                    else -> emptyInstruction()
                }
            } else if (this[0] == 9.toByte() && this[3] == 0.toByte()) {
                return constructInstruction("9XY0", x.toShort(), y.toShort())
            } else if (this[0] == 0xa.toByte()) {
                return constructInstruction("ANNN", nnn)
            } else if (this[0] == 0xb.toByte()) {
                return constructInstruction("BNNN", nnn)
            } else if (this[0] == 0xc.toByte()) {
                return constructInstruction("CXKK", x.toShort(), leastSignificantByte.toShort())
            } else if (this[0] == 0xd.toByte()) {
                return constructInstruction("DXYN", x.toShort(), y.toShort(), mostSignificantNibble.toShort())
            } else if (this[0] == 0xe.toByte() && this[2] == 9.toByte() && this[3] == 0xe.toByte()) {
                return constructInstruction("EX9E", x.toShort())
            } else if (this[0] == 0xe.toByte() && this[2] == 0xa.toByte() && this[3] == 1.toByte()) {
                return constructInstruction("EXA1", x.toShort())
            } else if (this[0] == 0xf.toByte()) {
                return when (this.mostSignificantByte) {
                    0x07.toByte() -> constructInstruction("FX07", x.toShort())
                    0x0a.toByte() -> constructInstruction("FX0A", x.toShort())
                    0x15.toByte() -> constructInstruction("FX15", x.toShort())
                    0x18.toByte() -> constructInstruction("FX18", x.toShort())
                    0x1e.toByte() -> constructInstruction("FX1E", x.toShort())
                    0x29.toByte() -> constructInstruction("FX29", x.toShort())
                    0x33.toByte() -> constructInstruction("FX33", x.toShort())
                    0x55.toByte() -> constructInstruction("FX55", x.toShort())
                    0x65.toByte() -> constructInstruction("FX65", x.toShort())
                    else -> emptyInstruction()
                }
            }
        }
        return emptyInstruction()
    }

    override fun parse(bytes: ByteArray, digest: (Instruction) -> Unit) {
        for (i in bytes.indices step 2) {
            val instruction = this.parse(bytes[i] to bytes[i + 1])
            digest(instruction)
        }
    }

}

fun parsingEngine(): ParsingEngine = ParsingEngineImpl