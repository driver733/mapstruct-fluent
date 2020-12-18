package com.driver733.mapstructfluent

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.compilation.shouldNotCompile
import io.kotest.matchers.shouldBe

import org.mapstruct.factory.Mappers

class EmployeeMapperTest : FunSpec({

    test("mapstruct impl") {
        val model = EmployeeModel("Alex")

        model
            .toEmployeeView()
            .shouldBe(
                Mappers
                    .getMapper(EmployeeMapper::class.java)
                    .toEmployeeView(model)
            )
    }

    test("ignore BeforeMapping impl") {
        Mappers
            .getMapper(EmployeeMapper::class.java)
            .toEmployeeViewBefore()

        """
            package com.driver733.mapstructfluent
            class A {
                fun b() {
                    EmployeeModel("Alex").toEmployeeViewBefore()
                }
           }
        """.trimIndent()
            .shouldNotCompile()
    }

    test("ignore AfterMapping impl") {
        Mappers
            .getMapper(EmployeeMapper::class.java)
            .toEmployeeViewAfter()

        """
            package com.driver733.mapstructfluent
            class A {
                fun b() {
                    EmployeeModel("Alex").toEmployeeViewAfter()
                }
           }
        """.trimIndent()
            .shouldNotCompile()
    }

})
