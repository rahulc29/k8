package org.waoss.k8.javafx

import javafx.scene.Scene
import org.waoss.k8.input.Key
import org.waoss.k8.input.Keyboard

class FXKeyboard(private val scene: Scene) : Keyboard {
    private var keyLastPressedName: String = ""

    init {
        scene.setOnKeyPressed {
            val codeName = it.code.name
            if (codeName != keyLastPressedName) {
                keyLastPressedName = it.code.name.digitToNum()
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