package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.search.EmployeeSearchRequest;
import com.carloprogram.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@PreAuthorize("hasAuthority('ADMIN')")
public class EmployeeAdminController {
    private final EmployeeService employeeService;

    public EmployeeAdminController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee (@Valid @RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //Build get all employees rest api
    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(@ModelAttribute EmployeeSearchRequest request) {
        Page<EmployeeDto> employees = employeeService.getAllEmployees(request);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long id) {
        EmployeeDto employees = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employees);
    }

    //Build update employee rest api
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,
                                                      @RequestBody EmployeeDto updatedEmployee){
        EmployeeDto employeeDto = employeeService.updateEmployeeById(employeeId, updatedEmployee);
        return ResponseEntity.ok(employeeDto);
    }

    @PutMapping("/{id}/assign-roles")
    public ResponseEntity<EmployeeDto> assignRolesToEmployee(
            @PathVariable("id") Long employeeId,
            @RequestBody List<Long> roleIds) {
        EmployeeDto updatedEmployee = employeeService.assignRoleToEmployee(employeeId, roleIds);
        return ResponseEntity.ok(updatedEmployee);
    }

    //Build delete employee rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployeeById(employeeId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee marked as deleted");
        response.put("employeeId", employeeId);
        return ResponseEntity.ok(response);
    }
}
