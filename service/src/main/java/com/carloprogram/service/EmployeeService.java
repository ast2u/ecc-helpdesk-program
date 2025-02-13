package com.carloprogram.service;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployeeById(Long employeeId);
    List<EmployeeDto> getAllEmployees();
    EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee);
    void deleteEmployeeById(Long employeeId);
    //EmployeeDto assignRoleToEmployee(Long employeeId, Long employeeRoleId);
}
