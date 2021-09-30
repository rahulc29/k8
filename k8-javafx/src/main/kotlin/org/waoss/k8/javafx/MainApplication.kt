package org.waoss.k8.javafx

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.waoss.k8.cpu.defaultProcessorContext
import org.waoss.k8.gpu.positionOf
import org.waoss.k8.input.Key
import org.waoss.k8.input.Keyboard

class MainApplication : Application() {
    override fun start(primaryStage: Stage?) {
        val canvas = FXGraphicsContext(64, 32)
        val container = StackPane(canvas)
        canvas.draw(positionOf(69, 69), 1)
        canvas.render()
        primaryStage?.let {
            it.title = "K8 Emulator"
            it.scene = Scene(container)
            it.show()
        }
        runBlocking {
            launch {

            }
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}