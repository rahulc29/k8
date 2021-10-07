package org.waoss.k8.cpu

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EngineTest {

    @Test
    fun initIOEngine() {
    }

    @Test
    fun bitwiseOperators() {
        val number = 0xabcd.toShort()
        assertEquals(expected = 0x0b.toByte(), actual = number.x)
        assertEquals(expected = 0x0c.toByte(), actual = number.y)
        assertEquals(expected = 0x0bcd.toShort(), actual = number.nnn)
    }
}