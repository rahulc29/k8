package org.waoss.k8.javafx

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.waoss.k8.ApplicationContext
import org.waoss.k8.cpu.constructInstruction
import org.waoss.k8.cpu.defaultProcessorContext
import org.waoss.k8.cpu.lazyExecutionEngine
import org.waoss.k8.cpu.parsingEngine
import org.waoss.k8.gpu.positionOf
import org.waoss.k8.io.FileIOEngine

class MainApplication : Application() {
    override fun start(primaryStage: Stage?) {
        val canvas = FXGraphicsContext(width = 64, height = 32)
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
            val fileChooser = FileChooser()
            fileChooser.title = "Open ROM"
            val file = fileChooser.showOpenDialog(it)
            val context = ApplicationContext(
                graphicsContext = canvas,
                processorContext = processorContext,
                executionEngine = processorContext.lazyExecutionEngine().value,
                ioEngine = FileIOEngine(file),
                parsingEngine = parsingEngine()
            )
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                context.executionLoop()
            }
        }
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}