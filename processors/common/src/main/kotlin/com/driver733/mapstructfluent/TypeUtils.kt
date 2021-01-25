package com.driver733.mapstructfluent

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.annotations.Nullable
import javax.lang.model.element.Element
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

@SuppressWarnings("SpreadOperator")
fun className(vararg elements: Element) =
    ClassName("", *elements.map { it.simpleName.toString() }.toTypedArray())

fun Element.kotlinType() =
    kotlinTypeWithInferredNullability()

private fun Element.kotlinTypeWithInferredNullability() =
    typeToKotlinType()
        .let { if (isNullable()) it.copy(true) else it.copy(false) }

private fun Element.typeToKotlinType() =
    asType().asTypeName().toKotlinType()

fun Element.isNullable() =
    this.getAnnotation(Nullable::class.java) != null

@SuppressWarnings("SpreadOperator")
fun TypeName.toKotlinType(): TypeName =
    when (this) {
        is ParameterizedTypeName -> {
            (rawType.toKotlinType() as ClassName).parameterizedBy(
                *typeArguments.map {
                    it.toKotlinType()
                }.toTypedArray()
            )
        }
        is WildcardTypeName -> {
            val type =
                if (inTypes.isNotEmpty()) WildcardTypeName.consumerOf(inTypes[0].toKotlinType())
                else WildcardTypeName.producerOf(outTypes[0].toKotlinType())
            type
        }
        else -> {
            val className =
                JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(
                    FqName(toString())
                )?.asSingleFqName()?.asString()
            if (className == null) {
                this
            } else {
                ClassName.bestGuess(className)
            }
        }
    }

fun Element.toParameterSpec() =
    ParameterSpec(simpleName.toString(), kotlinType(), emptyList())

fun List<ParameterSpec>.joinToString() =
    if (isNotEmpty()) joinToString(prefix = ", ", transform = ParameterSpec::name) else ""
