package com.carloprogram.service;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.EmployeeRoleDto;

import java.util.List;
import java.util.Set;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployeeById(Long employeeId);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee);
    void deleteEmployeeById(Long employeeId);
    //EmployeeDto assignRoleToEmployee(Long employeeId, Set<Long> roleIds);
}
