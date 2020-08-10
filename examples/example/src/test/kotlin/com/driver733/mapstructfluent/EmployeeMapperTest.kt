package com.driver733.mapstructfluent

import org.assertj.core.api.Assertions.assertThat
import org.mapstruct.factory.Mappers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object EmployeeMapperTest : Spek({

    Feature("Fluent extension fun") {
        Scenario("mapper") {
            val model = EmployeeModel("Alex")
            val actual = model.toEmployeeView()
            Then("Extension fun result is equal to mapper fun result") {
                assertThat(actual).isEqualTo(
                        Mappers.getMapper(EmployeeMapper::class.java).toEmployeeView(model)
                )
            }
        }
    }

})
