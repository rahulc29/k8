package org.waoss.k8.cpu

import org.waoss.k8.gpu.GraphicsContext

interface Context {
    val graphicsContext: GraphicsContext
}