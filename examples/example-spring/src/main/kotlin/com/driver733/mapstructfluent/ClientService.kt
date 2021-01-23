package com.driver733.mapstructfluent

import org.mapstruct.AfterMapping
import org.mapstruct.BeforeMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

data class EmployeeModel(val firstName: String)
data class EmployeeView(val firstName: String)
data class EmployeeCustomView(val firstName: String)
data class EmployeeCompanyView(val firstName: String, val company: String)

@Mapper(componentModel = "spring")
@SuppressWarnings("UnnecessaryAbstractClass", "EmptyFunctionBlock")
abstract class EmployeeMapper {

    abstract fun toEmployeeView(employeeModel: EmployeeModel): EmployeeView

    @Mappings(
        Mapping(target = "company", source = "company")
    )
    abstract fun toEmployeeCompanyView(employeeModel: EmployeeModel, company: String): EmployeeCompanyView

    @BeforeMapping
    fun toEmployeeViewBefore() {}

    @AfterMapping
    fun toEmployeeViewAfter() {}

    fun toEmployeeCustomView(employeeModel: EmployeeModel) =
        EmployeeCustomView(employeeModel.firstName)
}
