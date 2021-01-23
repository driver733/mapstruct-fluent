package com.driver733.mapstructfluent

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.compilation.shouldNotCompile
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EmployeeMapperTest(
    private val mapper: EmployeeMapper
) : FunSpec({

    test("mapstruct impl") {
        val model = EmployeeModel("Alex")

        val actual = model.toEmployeeView()

        actual.apply {
            shouldBe(EmployeeView("Alex"))
            shouldBe(mapper.toEmployeeView(model))
        }
    }

    test("mapstruct additional arguments") {
        val model = EmployeeModel("Alex")

        val actual = model.toEmployeeCompanyView("amazon")

        actual.apply {
            shouldBe(EmployeeCompanyView("Alex", "amazon"))
            shouldBe(mapper.toEmployeeCompanyView(model, "amazon"))
        }
    }

    test("custom mapper impl") {
        val model = EmployeeModel("Alex")

        model
            .toEmployeeCustomView()
            .shouldBe(
                mapper.toEmployeeCustomView(model)
            )
    }

    test("ignore BeforeMapping") {
        EmployeeMapper::class.java.getBean().toEmployeeViewBefore()

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

    test("ignore AfterMapping") {
        EmployeeMapper::class.java.getBean().toEmployeeViewAfter()

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
