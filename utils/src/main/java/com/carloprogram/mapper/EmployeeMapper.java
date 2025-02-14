package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.model.Employee;
import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.model.enums.EmploymentStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee){

        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAge(),
                employee.getAddress(),
                employee.getContactNumber(),
                employee.getEmploymentStatus(),
                employee.getEmployeeRoles()
                        .stream()
                        .map(EmployeeRoleMapper::mapToEmployeeRoleDto) // Convert role entities to IDs
                        .collect(Collectors.toSet())
        );
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto){
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setAge(employeeDto.getAge());
        employee.setAddress(employeeDto.getAddress());
        employee.setContactNumber(employeeDto.getContactNumber());
        employee.setEmploymentStatus(employeeDto.getEmploymentStatus());
        return employee;
    }
}
