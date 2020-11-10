package com.driver733.mapstructfluent

import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

fun fileSpecBuilder(procEnv: ProcessingEnvironment, mapper: Element, suffix: String) =
    FileSpec.builder(
        procEnv.elementUtils.getPackageOf(mapper).toString(),
        "${className(mapper).simpleName.capitalize()}$suffix"
    )

