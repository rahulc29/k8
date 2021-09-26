package org.waoss.k8.jfx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.let {
            val stackPane = StackPane()
            stackPane.children.add(TextField("Aur bhai?"))
            val scene = Scene(stackPane)
            it.scene = scene
            it.show()
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}