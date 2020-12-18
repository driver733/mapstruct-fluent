package com.driver733.mapstructfluent

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EmployeeMapperTest(
    private val mapper: EmployeeMapper
) : FunSpec({

    test("extension fun result should be equal to the mapper instance fun result") {
        val model = EmployeeModel("Alex")

        model
            .toEmployeeView()
            .shouldBe(
                mapper.toEmployeeView(model)
            )
    }

    test("extension fun result must be equal to the mapper instance fun result2") {
        val model = EmployeeModel("Alex")

        model
            .toEmployeeCustomView()
            .isEqualTo(
                mapper.toEmployeeCustomView(model)
            )
    }

    test("extension fun result must be equal to the mapper instance fun result3") {
        val model = EmployeeModel("Alex")

        model
            .toEmployeeCustomView()
            .isEqualTo(
                mapper.toEmployeeCustomView(model)
            )

    }

})
