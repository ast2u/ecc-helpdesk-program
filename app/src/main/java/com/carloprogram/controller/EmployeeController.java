package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.dto.search.EmployeeSearchRequest;
import com.carloprogram.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //Build App Employee Rest API
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeDto> createEmployee (@Valid @RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDto> getMyInfo() {
        EmployeeProfileDto employeeProfileDtoDto = employeeService.getEmployeeProfile();
        return ResponseEntity.ok(employeeProfileDtoDto);
    }

    //Build get all employees rest api
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(@ModelAttribute EmployeeSearchRequest request) {
        Page<EmployeeDto> employees = employeeService.getAllEmployees(request);
        return ResponseEntity.ok(employees);
    }

    //Build update employee rest api
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,
                                                      @RequestBody EmployeeDto updatedEmployee){
        EmployeeDto employeeDto = employeeService.updateEmployeeById(employeeId, updatedEmployee);
        return ResponseEntity.ok(employeeDto);
    }

    @PutMapping("/{id}/assign-roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeDto> assignRolesToEmployee(
            @PathVariable("id") Long employeeId,
            @RequestBody List<Long> roleIds) {
        EmployeeDto updatedEmployee = employeeService.assignRoleToEmployee(employeeId, roleIds);
        return ResponseEntity.ok(updatedEmployee);
    }

    //Build delete employee rest api
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok("Employee#" + employeeId + " marked as deleted");
    }

}
