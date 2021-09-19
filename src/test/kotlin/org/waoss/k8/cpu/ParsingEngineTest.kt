package org.waoss.k8.cpu

import kotlin.test.Test
import kotlin.test.assertEquals

class ParsingEngineTest {

    @Test
    fun parseSingleInstruction() {
        val parser = parsingEngine()
        assertEquals(
            expected = constructInstruction("EX9E", 0xe.toShort()),
            actual = parser.parse(Pair(0xee.toByte(), 0x9e.toByte()))
        )

    }
}