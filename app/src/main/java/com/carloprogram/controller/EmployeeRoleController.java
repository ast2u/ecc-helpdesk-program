package com.carloprogram.controller;


import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.service.EmployeeRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee_roles")
public class EmployeeRoleController {

    @Autowired
    private EmployeeRoleService employeeRoleService;

    //Build Create role Rest API
    @PostMapping
    public ResponseEntity<EmployeeRoleDto> createEmployeeRole (@Valid @RequestBody EmployeeRoleDto employeeRoleDto){
        EmployeeRoleDto savedEmployee = employeeRoleService.createEmployeeRole(employeeRoleDto);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    //Get role by id rest api
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRoleDto> getEmployeeById(@PathVariable("id") Long employeeRoleId){
        EmployeeRoleDto employeeRoleDto = employeeRoleService.getEmployeeRoleById(employeeRoleId);
        return ResponseEntity.ok(employeeRoleDto);
    }

    //Build get all role rest api
    @GetMapping
    public ResponseEntity<List<EmployeeRoleDto>> getEmployeeRoles(){
        List<EmployeeRoleDto> employeeRoles = employeeRoleService.getAllEmployeeRoles();
        return ResponseEntity.ok(employeeRoles);
    }

    //Build update role rest api
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRoleDto> updateEmployeeRole(@PathVariable("id") Long employeeRoleId,@Valid @RequestBody EmployeeRoleDto updatedEmployeeRole){
        EmployeeRoleDto employeeRoleDto = employeeRoleService.updateEmployeeRoleById(employeeRoleId, updatedEmployeeRole);
        return ResponseEntity.ok(employeeRoleDto);
    }

    //Build delete role rest api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeRole(@PathVariable("id") Long employeeRoleId){
        // fix if possible
        try{
            EmployeeRoleDto employeeRole = employeeRoleService.getEmployeeRoleById(employeeRoleId);
            String roleTitle = employeeRole.getRole_title();
            employeeRoleService.deleteEmployeeRoleById(employeeRoleId);
            return ResponseEntity.ok("Role name: "+ roleTitle +" deleted successfully!");
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: "+ ex.getMessage());
        }

    }


}
