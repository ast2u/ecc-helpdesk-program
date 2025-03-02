package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    //Get employee by id rest api
    //Can be deleted
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId){
//        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);
//        return ResponseEntity.ok(employeeDto);
//    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeDto> getMyInfo(Authentication authentication) {
        EmployeeDto employeeDto = employeeService.getEmployeeProfile(authentication);
        return ResponseEntity.ok(employeeDto);
    }

    //Build get all employees rest api
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "4", name = "size") int size) {

        Page<EmployeeDto> employees = employeeService.getAllEmployees(page, size);
        return ResponseEntity.ok(employees);
    }

    //Build update employee rest api
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,@Valid @RequestBody EmployeeDto updatedEmployee){
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
        try {
            employeeService.deleteEmployeeById(employeeId);
            return ResponseEntity.ok("Employee#" + employeeId + " marked as deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
        }
    }

}
