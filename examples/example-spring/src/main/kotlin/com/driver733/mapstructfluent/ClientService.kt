package com.driver733.mapstructfluent

import org.mapstruct.Mapper

data class EmployeeModel(private val firstName: String? = null)
data class EmployeeView(private val firstName: String? = null)

@Mapper(componentModel = "spring")
@SuppressWarnings("UnnecessaryAbstractClass")
abstract class EmployeeMapper {
    abstract fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView
}
