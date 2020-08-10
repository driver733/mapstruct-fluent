package com.driver733.mapstructfluent

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class ApplicationContextService(
        private val context: ApplicationContext
) {
    companion object {
        private lateinit var context: ApplicationContext

        fun <T> getBean(clazz: Class<T>) = context.getBean(clazz)
    }

    @PostConstruct
    fun initialize() {
        Companion.context = context
    }
}

fun <T> Class<T>.getBean() = ApplicationContextService.getBean(this)

