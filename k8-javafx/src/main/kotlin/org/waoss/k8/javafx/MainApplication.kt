package org.waoss.k8.javafx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.let {
            it.title = "Bruh"
            it.scene = Scene(StackPane(TextField("Bruh")))
            it.show()
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}