package com.carloprogram.mapper;


import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.model.EmployeeRole;

public class EmployeeRoleMapper {
    public static EmployeeRoleDto mapToEmployeeRoleDto(EmployeeRole employeeRole){
        return new EmployeeRoleDto(
                employeeRole.getRole_id(),
                employeeRole.getRole_title(),
                employeeRole.getRole_description()
        );
    }

    public static EmployeeRole mapToEmployeeRole(EmployeeRoleDto employeeRoleDto){
        return new EmployeeRole(
                employeeRoleDto.getRole_id(),
                employeeRoleDto.getRole_title(),
                employeeRoleDto.getRole_description()
        );
    }


}
