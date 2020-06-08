import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.mapstruct.AfterMapping
import org.mapstruct.BeforeMapping
import org.mapstruct.Mapper
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.*
import javax.tools.Diagnostic

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

fun isMappingMethod(): (ExecutableElement) -> Boolean = { method ->
    method.annotationMirrors.none {
        listOf<TypeName>(
                BeforeMapping::class.asTypeName(), AfterMapping::class.asTypeName()
        ).contains(
                it.annotationType.asElement().asType().asTypeName().toKotlinType()
        )
    }
}

fun process(roundEnv: RoundEnvironment?, procEnv: ProcessingEnvironment, processor: (method: ExecutableElement, mapper: Element, src: String?) -> Unit) =
        generatedSourcesDirPath(procEnv)
                .also { if (it == null) return false }
                .let { src -> findAndProcessMappers(roundEnv, src, processor) }
                .let { true }

fun findAndProcessMappers(env: RoundEnvironment?, src: String?, processor: (method: ExecutableElement, mapper: Element, src: String?) -> Unit) {
    env?.getElementsAnnotatedWith(Mapper::class.java)?.forEach { mapper ->
        mapper.enclosedElements
                .filter { it.kind == ElementKind.METHOD }
                .map { it as ExecutableElement }
                .filter(isMappingMethod())
                .forEach {
                    processor(it, mapper, src)
                }
    }
}

fun generatedSourcesDirPath(env: ProcessingEnvironment): String? =
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

fun Name.capitalize() = this.toString().capitalize()

fun FileSpec.Companion.builder(fileName: String) = builder("", fileName)

fun className(vararg simpleNames: String) = ClassName("", *simpleNames)

fun VariableElement.kotlinType() = this.asType().asTypeName().toKotlinType()

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
                val className = kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(kotlin.reflect.jvm.internal.impl.name.FqName(toString()))?.asSingleFqName()?.asString()
                if (className == null) {
                    this
                } else {
                    ClassName.bestGuess(className)
                }
            }
        }