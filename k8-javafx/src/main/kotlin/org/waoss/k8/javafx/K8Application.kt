package org.waoss.k8.javafx

import javafx.application.Application
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Scene
import javafx.scene.input.KeyEvent
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.waoss.k8.input.Key
import org.waoss.k8.input.Keyboard

class K8Application : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.let {
            val parent = StackPane()
            val scene = Scene(parent)
            val keyboard = object : Keyboard {
                override fun isPressed(key: Key): Boolean {
                    TODO("Not yet implemented")
                }

                override fun nextKey(): Key {
                    TODO("Not yet implemented")
                }
            }
            scene.setOnKeyPressed {

            }
        }
    }
}

fun main() {
    Application.launch(K8Application::class.java)
}