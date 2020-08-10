package com.driver733.mapstructfluent

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("org.mapstruct.Mapper")
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
class MapstructSpringFluentExtensionsAnnotationProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?) =
            process(roundEnv, processingEnv, this::processMapper)

    private fun processMapper(method: ExecutableElement, mapper: Element, src: String?) {
        FileSpec.builder(
                processingEnv.elementUtils.getPackageOf(method).toString(),
                "${className(mapper).simpleName.capitalize()}$${method.simpleName.capitalize()}FluentSpringExtensions"
        ).addImport(
                processingEnv.elementUtils.getPackageOf(method).toString(),
                className(mapper).simpleName
        ).addImport(
                "com.driver733.mapstructfluent", "getBean"
        ).addFunction(
                FunSpec.builder("${method.simpleName}")
                        .receiver(method.parameters.first().kotlinType())
                        .addStatement(
                                "return ${mapper.simpleName}::class.java.getBean().${method.simpleName}(this)"
                        )
                        .build()
        ).build().writeTo(
                File(src!!).apply { mkdir() }
        )
    }

}

