package com.carloprogram.service;

import com.carloprogram.dto.changerequest.ChangeCredentialRequest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EmployeeCredentialsUpdateService {
    ResponseEntity<Map<String, Object>> changeCredentials(ChangeCredentialRequest request);
}
