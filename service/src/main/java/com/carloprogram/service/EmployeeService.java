package com.carloprogram.service;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.EmployeeRoleDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployeeById(Long employeeId);
    Page<EmployeeDto> getAllEmployees(int page, int size);
    EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee);
    void deleteEmployeeById(Long employeeId);
    EmployeeDto assignRoleToEmployee(Long employeeId, List<Long> roleIds);
}
