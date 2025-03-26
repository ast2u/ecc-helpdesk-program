package com.carloprogram.service;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.dto.search.EmployeeSearchRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import com.carloprogram.dto.login.LoginRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    ResponseEntity<?> authenticateUser(LoginRequest loginRequest, HttpServletResponse response);
    ResponseEntity<?> refreshTokenUser(String refreshToken);
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeProfileDto getEmployeeProfile();
    List<String> getEmployeeRoles();
    EmployeeDto getEmployeeById(Long employeeId);
    EmployeeProfileDto updateEmployeeProfile(EmployeeProfileDto profileDto);
    Page<EmployeeDto> getAllEmployees(EmployeeSearchRequest searchRequest);
    EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee);
    void deleteEmployeeById(Long employeeId);
    EmployeeDto assignRoleToEmployee(Long employeeId, List<Long> roleIds);
}
