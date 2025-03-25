package com.carloprogram.service.impl;

import com.carloprogram.dto.changerequest.ChangeCredentialRequest;
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
import java.util.Optional;

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
    public ResponseEntity<Map<String, Object>> changeCredentials(ChangeCredentialRequest request) {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        Employee employee = employeeRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not editable"));

        Map<String, Object> response = new HashMap<>();
        boolean usernameChanged = false;

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            Optional<Employee> existingUser = employeeRepository.findByUsernameAndDeletedFalse(request.getUsername());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(employee.getId())) {
                response.put("message", "Username is already taken");
                response.put("status", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!employee.getUsername().equals(request.getUsername())) {
                employee.setUsername(request.getUsername());
                usernameChanged = true;
            }
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            if (request.getOldPassword() == null || !encoder.matches(request.getOldPassword(), employee.getPassword())) {
                response.put("message", "Old password is incorrect");
                response.put("status", HttpStatus.UNAUTHORIZED.value());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            employee.setPassword(encoder.encode(request.getNewPassword()));
        }

        employeeRepository.save(employee);

        response.put("message", "Password changed successfully");
        response.put("status", HttpStatus.OK.value());
        response.put("logout", usernameChanged);

        return ResponseEntity.ok(response);
    }
}
