package com.carloprogram.mapper;

import com.carloprogram.model.Employee;
import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.model.enums.EmploymentStatus;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee){
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAge(),
                employee.getAddress(),
                employee.getContactNumber(),
                employee.getEmploymentStatus().name(),
                employee.getEmployeeRoleId() //fix
        );
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto){
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getAge(),
                employeeDto.getAddress(),
                employeeDto.getContactNumber(),
                EmploymentStatus.valueOf(employeeDto.getEmploymentStatus()),
                employeeDto.getEmployeeRoleId() //fix

        );
    }
}
