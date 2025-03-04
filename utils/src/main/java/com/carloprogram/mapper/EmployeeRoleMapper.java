package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.model.EmployeeRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeRoleMapper {

    EmployeeRoleDto mapToEmployeeRoleDto(EmployeeRole employeeRole);

    @Mapping(target = "employees", ignore = true)
    EmployeeRole mapToEmployeeRole(EmployeeRoleDto employeeRoleDto);

}
