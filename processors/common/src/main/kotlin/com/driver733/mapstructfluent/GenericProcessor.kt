package com.driver733.mapstructfluent

import org.mapstruct.Mapper
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.ABSTRACT
import javax.lang.model.element.Modifier.PUBLIC
import javax.tools.Diagnostic

class GenericProcessor {

    fun process(roundEnv: RoundEnvironment?, procEnv: ProcessingEnvironment, processor: MapperMethodProcessor) =
        generatedSourcesDirPath(procEnv)
            .also { if (it == null) return false }
            .let { src -> findAndProcessMappers(roundEnv, src, processor) }
            .let { true }

    private fun generatedSourcesDirPath(env: ProcessingEnvironment) =
        env.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            .let {
                if (it == null) {
                    env.messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "Can't find the target directory for generated Kotlin files"
                    )
                    null
                } else {
                    it
                }
            }

    private fun findAndProcessMappers(
        roundEnv: RoundEnvironment?,
        src: String?,
        processor: MapperMethodProcessor
    ) =
        roundEnv?.getElementsAnnotatedWith(Mapper::class.java)?.forEach { mapper ->
            mapper
                .takeIf { it.modifiers.contains(ABSTRACT) }
                ?.enclosedElements
                ?.filter { it.kind == ElementKind.METHOD }
                ?.filter { it.modifiers.contains(PUBLIC) }
                ?.filter { it.modifiers.contains(ABSTRACT) }
                ?.map { it as ExecutableElement }
                ?.also { methods ->
                    val fileSpecBuilder = processor.fileSpecBuilder(mapper)
                    methods.forEach {
                        processor.process(fileSpecBuilder, it, mapper, src)
                    }
                }
        }

}
