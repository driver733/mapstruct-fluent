package com.driver733.mapstructfluent

import org.mapstruct.AfterMapping
import org.mapstruct.BeforeMapping
import org.mapstruct.Mapper

data class EmployeeModel(val firstName: String? = null)
data class EmployeeView(val firstName: String? = null)

@Mapper
@Suppress("EmptyFunctionBlock")
abstract class EmployeeMapper {

    abstract fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView

    @BeforeMapping
    fun toEmployeeViewBefore() {}

    @AfterMapping
    fun toEmployeeViewAfter() {}
}
