package com.carloprogram.service;

import com.carloprogram.dto.changerequest.ChangePasswordRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EmployeeCredentialsUpdateService {
    ResponseEntity<Map<String, Object>> changePassword(ChangePasswordRequest request);
    ResponseEntity<String> changeUsername(String newUsername);
}
