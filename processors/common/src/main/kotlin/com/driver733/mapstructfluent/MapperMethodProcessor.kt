package com.driver733.mapstructfluent

import com.squareup.kotlinpoet.FileSpec
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement

interface MapperMethodProcessor {
    fun process(fileSpecBuilder: FileSpec.Builder, method: ExecutableElement, mapper: Element, src: String?)
    fun fileSpecBuilder(mapper: Element): FileSpec.Builder
}
