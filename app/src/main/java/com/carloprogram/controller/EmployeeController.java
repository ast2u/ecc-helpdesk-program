package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.EmployeeProfileDto;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    //Build App Employee Rest API
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeDto> createEmployee (@Valid @RequestBody EmployeeDto employeeDto){
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<EmployeeProfileDto> getMyInfo() {
        EmployeeProfileDto employeeProfileDtoDto = employeeService.getEmployeeProfile();
        return ResponseEntity.ok(employeeProfileDtoDto);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<String>> getUserRoles(){
        return ResponseEntity.ok(employeeService.getEmployeeRoles());
    }

    @PatchMapping("/profile/edit")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<EmployeeProfileDto> updateProfile(@Valid @RequestBody EmployeeProfileDto employeeProfileDto) {
        EmployeeProfileDto employeeProfileDtoDto = employeeService.updateEmployeeProfile(employeeProfileDto);
        return ResponseEntity.ok(employeeProfileDtoDto);
    }

    //Build get all employees rest api
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(@ModelAttribute EmployeeSearchRequest request) {
        Page<EmployeeDto> employees = employeeService.getAllEmployees(request);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long id) {
        EmployeeDto employees = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employees);
    }

    //Build update employee rest api
    @PutMapping("/{id}")
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
    public ResponseEntity<Map<String,Object>> deleteEmployee(@PathVariable("id") Long employeeId){
        employeeService.deleteEmployeeById(employeeId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee marked as deleted");
        response.put("employeeId", employeeId);
        return ResponseEntity.ok(response);
    }

}
