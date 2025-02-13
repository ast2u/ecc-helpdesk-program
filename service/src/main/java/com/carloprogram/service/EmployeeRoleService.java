package com.carloprogram.service;

import com.carloprogram.dto.EmployeeRoleDto;

import java.util.List;

public interface EmployeeRoleService {
    EmployeeRoleDto createEmployeeRole(EmployeeRoleDto employeeRoleDto);
    EmployeeRoleDto getEmployeeRoleById(Long employeeRoleId);
    List<EmployeeRoleDto> getAllEmployeeRoles();
    EmployeeRoleDto updateEmployeeRoleById(Long employeeRoleId, EmployeeRoleDto updatedEmployeeRole);
    void deleteEmployeeRoleById(Long employeeRoleId);
}
