package com.driver733.mapstructfluent.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.listeners.Listener
import io.kotest.spring.SpringAutowireConstructorExtension
import io.kotest.spring.SpringListener

object ProjectConfig : AbstractProjectConfig() {
    override fun extensions(): List<Extension> = listOf(SpringAutowireConstructorExtension)
    override fun listeners(): List<Listener> = listOf(SpringListener)
}
