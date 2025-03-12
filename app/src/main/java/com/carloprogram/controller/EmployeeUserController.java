package com.carloprogram.controller;

import com.carloprogram.dto.login.LoginRequest;
import com.carloprogram.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeUserController {

    private final EmployeeService employeeService;

    public EmployeeUserController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return employeeService.authenticateUser(loginRequest);
    }
}
