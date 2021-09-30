package org.waoss.k8.javafx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.waoss.k8.gpu.positionOf

class MainApplication : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.let {
            it.title = "K8 Emulator"
            val canvas = FXGraphicsContext(64, 32)
            val container = StackPane(canvas)
            canvas.draw(positionOf(69, 69), 1)
            canvas.render()
            it.scene = Scene(container)
            it.show()
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}