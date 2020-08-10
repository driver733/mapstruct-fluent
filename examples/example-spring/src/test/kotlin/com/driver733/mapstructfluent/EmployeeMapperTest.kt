package com.driver733.mapstructfluent

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EmployeeMapperTest @Autowired constructor(
        private val mapper: EmployeeMapper
) {

    @Test
    fun `extension fun result must be equal to the mapper instance fun result`() {
        val model = EmployeeModel("Alex")
        assertThat(
                model.toEmployeeView()
        ).isEqualTo(
                mapper.toEmployeeView(model)
        )
    }

}


