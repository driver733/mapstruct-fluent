package com.driver733.mapstructfluent

import org.mapstruct.Mapper

data class EmployeeModel(private val firstName: String? = null)
data class EmployeeView(private val firstName: String? = null)

@Mapper
interface EmployeeMapper {
    fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView
}
