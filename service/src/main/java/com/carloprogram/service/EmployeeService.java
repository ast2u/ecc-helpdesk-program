package com.carloprogram.service;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.model.EmployeeUserPrincipal;
import org.springframework.data.domain.Page;
import com.carloprogram.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface EmployeeService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);
    EmployeeDto createEmployee(EmployeeDto employeeDto, EmployeeUserPrincipal userPrincipal);
    EmployeeDto getEmployeeProfile(EmployeeUserPrincipal userPrincipal);
    //EmployeeDto getEmployeeById(Long employeeId);
    Page<EmployeeDto> getAllEmployees(int page, int size);
    EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee, EmployeeUserPrincipal userPrincipal);
    void deleteEmployeeById(Long employeeId);
    EmployeeDto assignRoleToEmployee(Long employeeId, List<Long> roleIds, EmployeeUserPrincipal userPrincipal);
}
