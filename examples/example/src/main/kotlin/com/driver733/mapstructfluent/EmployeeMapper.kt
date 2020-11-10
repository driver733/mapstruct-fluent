package com.driver733.mapstructfluent

import org.mapstruct.Mapper

data class EmployeeModel(val firstName: String? = null)
data class EmployeeView(val firstName: String? = null)

@Mapper
interface EmployeeMapper {
    fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView
}
