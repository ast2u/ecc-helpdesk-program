package com.carloprogram.controller;


import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.service.EmployeeRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employee_roles")
public class EmployeeRoleController {
    private EmployeeRoleService employeeRoleService;

    //Build App Employee Rest API
    @PostMapping
    public ResponseEntity<EmployeeRoleDto> createEmployeeRole (@RequestBody EmployeeRoleDto employeeRoleDto){
        EmployeeRoleDto savedEmployee = employeeRoleService.createEmployeeRole(employeeRoleDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //Get employee by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRoleDto> getEmployeeById(@PathVariable("id") Long employeeRoleId){
        EmployeeRoleDto employeeRoleDto = employeeRoleService.getEmployeeRoleById(employeeRoleId);
        return ResponseEntity.ok(employeeRoleDto);
    }

    //Build get all employees rest api
    @GetMapping
    public ResponseEntity<List<EmployeeRoleDto>> getEmployeeRoles(){
        List<EmployeeRoleDto> employeeRoles = employeeRoleService.getAllEmployeeRoles();
        return ResponseEntity.ok(employeeRoles);
    }

    //Build update employee rest api
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRoleDto> updateEmployeeRole(@PathVariable("id") Long employeeRoleId,@RequestBody EmployeeRoleDto updatedEmployeeRole){
        EmployeeRoleDto employeeRoleDto = employeeRoleService.updateEmployeeRoleById(employeeRoleId, updatedEmployeeRole);
        return ResponseEntity.ok(employeeRoleDto);
    }

    //Build delete employee rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeRole(@PathVariable("id") Long employeeRoleId){
        try{
            employeeRoleService.deleteEmployeeRoleById(employeeRoleId);
            return ResponseEntity.ok("Employee#"+ employeeRoleId +" deleted successfully!");
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: "+ ex.getMessage());
        }

    }


}
