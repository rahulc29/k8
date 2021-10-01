package org.waoss.k8.cpu

import org.waoss.k8.logger
import kotlin.test.Test
import kotlin.test.assertEquals

class ParsingEngineTest {

    @Test
    fun parseSingleInstruction() {
        val parser = parsingEngine()
        val parsed = parser.parse(Pair(0xee.toByte(), 0x9e.toByte()))
        assertEquals(
            expected = constructInstruction("EX9E", 0xe.toShort()),
            actual = parsed
        )
        assertEquals(expected = 14, actual = parsed.args[0])
        assertEquals(
            expected = constructInstruction("1NNN", 0x0eee),
            actual = parser.parse(Pair(0x1e.toByte(), 0xee.toByte()))
        )
    }

    @Test
    fun parseStreamOfInstructions() {
        val parser = parsingEngine()
        val bytes = byteArrayOf(
            0xee.toByte(), 0x9e.toByte()
        )
        parser.parse(bytes) {
            parser.logger.info("${it.name} parsed successfully with ${it.args.contentToString()} arguments")
        }
    }
}