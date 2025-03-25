package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.dto.changerequest.ChangeCredentialRequest;
import com.carloprogram.service.EmployeeCredentialsUpdateService;
import com.carloprogram.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeCredentialsUpdateService updateService;

    public EmployeeController(EmployeeService employeeService, EmployeeCredentialsUpdateService updateService) {
        this.employeeService = employeeService;
        this.updateService = updateService;
    }

    @GetMapping("/profile")
    public ResponseEntity<EmployeeProfileDto> getMyInfo() {
        EmployeeProfileDto employeeProfileDtoDto = employeeService.getEmployeeProfile();
        return ResponseEntity.ok(employeeProfileDtoDto);
    }

//    @GetMapping("/roles")
//    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
//    public ResponseEntity<List<String>> getUserRoles(){
//        return ResponseEntity.ok(employeeService.getEmployeeRoles());
//    }

    @PutMapping("/profile/edit")
    public ResponseEntity<EmployeeProfileDto> updateProfile(@Valid @RequestBody EmployeeProfileDto employeeProfileDto) {
        EmployeeProfileDto employeeProfileDtoDto = employeeService.updateEmployeeProfile(employeeProfileDto);
        return ResponseEntity.ok(employeeProfileDtoDto);
    }

    @PutMapping("profile/change-credentials")
    public ResponseEntity<Map<String,Object>> changeCredentials(@RequestBody ChangeCredentialRequest request) {
        return updateService.changeCredentials(request);
    }

}
