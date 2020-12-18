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

})
