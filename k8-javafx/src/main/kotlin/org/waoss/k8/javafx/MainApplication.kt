package org.waoss.k8.javafx

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.waoss.k8.cpu.constructInstruction
import org.waoss.k8.cpu.defaultProcessorContext
import org.waoss.k8.cpu.lazyExecutionEngine
import org.waoss.k8.gpu.positionOf

class MainApplication : Application() {
    override fun start(primaryStage: Stage?) {
        val canvas = FXGraphicsContext(64, 32)
        val container = StackPane(canvas)
        val scene = Scene(container)
        val keyboard = FXKeyboard(scene)
        val processorContext = defaultProcessorContext(canvas, keyboard)
        canvas.draw(positionOf(69, 69), 1)
        canvas.render()
        primaryStage?.let {
            it.title = "K8 Emulator"
            it.scene = scene
            it.show()
        }
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val lazyEngine = processorContext.lazyExecutionEngine()
            val engine = lazyEngine.value
            engine.execute(constructInstruction("DXYN", 1, 2, 3))
            canvas.draw(positionOf(20, 20), 1)
            Platform.runLater {
                canvas.render()
            }
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}