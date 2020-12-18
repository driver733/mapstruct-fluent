package com.driver733.mapstructfluent

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

import org.mapstruct.factory.Mappers

class EmployeeMapperTest : FunSpec({

    test("Extension fun result should be equal to mapper fun result") {
        val model = EmployeeModel("Alex")

        model
            .toEmployeeView()
            .shouldBe(
                Mappers
                    .getMapper(EmployeeMapper::class.java)
                    .toEmployeeView(model)
            )
    }

})
