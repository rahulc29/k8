package org.waoss.k8

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Loggable

inline val <reified T : Loggable> T.logger : Logger
    get() = LoggerFactory.getLogger(T::class.java)