package org.waoss.k8.javafx

import javafx.application.Application
import javafx.stage.Stage

class K8Application : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.let {

        }
    }
}

fun main() {
    Application.launch(K8Application::class.java)
}