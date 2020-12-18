package com.driver733.mapstructfluent

import org.mapstruct.AfterMapping
import org.mapstruct.BeforeMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget

data class EmployeeModel(val firstName: String? = null)
data class EmployeeView(val firstName: String? = null)
data class EmployeeCustomView(val firstName: String? = null)

@Mapper(componentModel = "spring")
@SuppressWarnings("UnnecessaryAbstractClass")
abstract class EmployeeMapper {

    abstract fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView

    @BeforeMapping
    fun toEmployeeViewBefore(
        @MappingTarget
        employeeView: EmployeeView
    ) {}

    @AfterMapping
    fun toEmployeeViewAfter(
        @MappingTarget
        employeeView: EmployeeView
    ) {}

    fun toEmployeeCustomView(employeeModel: EmployeeModel) =
        EmployeeCustomView(employeeModel.firstName)

}
