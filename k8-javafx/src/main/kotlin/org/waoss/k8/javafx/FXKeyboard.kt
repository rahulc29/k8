package org.waoss.k8.javafx

import javafx.scene.Scene
import org.waoss.k8.input.Key
import org.waoss.k8.input.Keyboard
import org.waoss.k8.logger

class FXKeyboard(private val scene: Scene) : Keyboard {
    private var keyLastPressedName: String = ""
    private val keyPressMap = mutableMapOf<String, Boolean>()
    private val keyMap = mapOf(
        "DIGIT1" to "NUM1",
        "DIGIT2" to "NUM2",
        "DIGIT3" to "NUM3",
        "DIGIT4" to "C"
    )

    init {
        scene.setOnKeyPressed {
            val codeName = it.code.name
            if (codeName != keyLastPressedName) {
                keyLastPressedName = it.code.name.digitToNum()
                logger.info("Key $codeName pressed")
            }
        }
    }

    override fun isPressed(key: Key): Boolean {
        return keyLastPressedName == key.name
    }

    private fun String.digitToNum(): String {
        return if (this.startsWith("DIGIT")) {
            "NUM${this.removePrefix("DIGIT")}"
        } else {
            this
        }
    }

    override fun nextKey(): Key {
        return Key.valueOf(keyLastPressedName.digitToNum())
    }
}