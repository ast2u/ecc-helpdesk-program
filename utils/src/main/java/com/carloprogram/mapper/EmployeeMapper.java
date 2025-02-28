package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.model.Employee;
import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.model.enums.EmploymentStatus;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee){

        List<EmployeeRoleDto> employeeRoleDtos = (employee.getEmployeeRoles() != null) ?
                employee.getEmployeeRoles()
                        .stream()
                        .map(EmployeeRoleMapper::mapToEmployeeRoleDto)
                        .collect(Collectors.toList())
                : null;

        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getBirthDate(),
                employee.getUsername(), //can include password for view
                employee.getAddress(),
                employee.getContactNumber(),
                employee.getEmploymentStatus(),
                employeeRoleDtos

        );
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto){
        Employee employee = new Employee();
        employee.setId(employeeDto.getId());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setUsername(generateUsername(employeeDto.getLastName())); //edit
        employee.setAddress(employeeDto.getAddress());
        employee.setContactNumber(employeeDto.getContactNumber());
        employee.setEmploymentStatus(employeeDto.getEmploymentStatus());
        return employee;
    }

    private static String generateUsername (String lastName){
        SecureRandom random = new SecureRandom();
        int randomN = 1000 + random.nextInt(9000);
        return lastName.toLowerCase() + randomN;
    }
}
