package com.carloprogram.mapper;


import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.model.EmployeeRole;

import java.util.Set;

public class EmployeeRoleMapper {
    public static EmployeeRoleDto mapToEmployeeRoleDto(EmployeeRole employeeRole){
        return new EmployeeRoleDto(
                employeeRole.getId(),
                employeeRole.getRole_title(),
                employeeRole.getRole_description()
        );
    }

    public static EmployeeRole mapToEmployeeRole(EmployeeRoleDto employeeRoleDto){
        return new EmployeeRole(
                employeeRoleDto.getId(),
                employeeRoleDto.getRole_title(),
                employeeRoleDto.getRole_description(),
                Set.of()
        );
    }


}
