package com.driver733.mapstructfluent

import KAPT_KOTLIN_GENERATED_OPTION_NAME
import builder
import capitalize
import className
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.asTypeName
import kotlinType
import process
import toKotlinType
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
                "${method.simpleName.capitalize()}FluentSpringExtensions"
        ).addImport(
                processingEnv.elementUtils.getPackageOf(method).toString(),
                className(mapper.simpleName.toString()).simpleName
        ).addImport(
                "com.driver733.mapstructfluent", "getBean"
        ).addFunction(
                FunSpec.builder("${method.simpleName}")
                        .receiver(method.parameters.first().kotlinType())
                        .addStatement(
                                "return ${mapper.simpleName}::class.java.getBean().${method.simpleName}(this)"
                        )
                        .returns(method.returnType.asTypeName().toKotlinType())
                        .build()
        ).build().writeTo(
                File(src!!).apply { mkdir() }
        )
    }

}

