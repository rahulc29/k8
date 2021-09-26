package org.waoss.k8.cpu

import org.waoss.k8.io.IOEngine
import kotlin.test.*
import kotlin.test.Test

class EngineTest {

    @Test
    fun initIOEngine() {
        val engine = object : IOEngine {
            override fun readAll(): ByteArray {
                return byteArrayOf(
                    0x00,
                    0xFF.toByte(),
                    0xaf.toByte()
                )
            }
        }
        val memory = engine.constructMemory()
        assertEquals(expected = 3, actual = memory.size)
        assertEquals(expected = 0, actual = memory[0])
    }

    @Test
    fun bitwiseOperators() {
        val number = 0xabcd.toShort()
        assertEquals(expected = 0x0b.toByte(), actual = number.x)
        assertEquals(expected = 0x0c.toByte(), actual = number.y)
        assertEquals(expected = 0x0bcd.toShort(), actual = number.nnn)
    }
}