package com.carloprogram.service.impl;

import com.carloprogram.dto.changerequest.ChangePasswordRequest;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.model.Employee;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.security.config.SecurityUtil;
import com.carloprogram.service.EmployeeCredentialsUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeCredentialsUpdateServiceImpl implements EmployeeCredentialsUpdateService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder encoder;
    private final SecurityUtil securityUtil;

    public EmployeeCredentialsUpdateServiceImpl(EmployeeRepository employeeRepository, BCryptPasswordEncoder encoder, SecurityUtil securityUtil) {
        this.employeeRepository = employeeRepository;
        this.encoder = encoder;
        this.securityUtil = securityUtil;
    }

    @Override
    @LogExecution
    public ResponseEntity<Map<String, Object>> changePassword(ChangePasswordRequest request) {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        Employee employee = employeeRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not editable"));

        if (!encoder.matches(request.getOldPassword(), employee.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Old password is incorrect");
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        employee.setPassword(encoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        response.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> changeUsername(String newUsername) {
        return null;
    }
}
