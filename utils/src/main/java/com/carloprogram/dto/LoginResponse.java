package com.carloprogram.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private EmployeeDto employee;

    public LoginResponse(String token, EmployeeDto employee) {
        this.token = token;
        this.employee = employee;
    }
}
