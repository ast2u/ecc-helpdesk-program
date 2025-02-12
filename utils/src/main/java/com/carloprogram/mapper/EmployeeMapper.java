package com.carloprogram.mapper;

import com.carloprogram.model.Employee;
import com.carloprogram.dto.EmployeeDto;

public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee){
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAge(),
                employee.getAddress(),
                employee.getContactNumber(),
                employee.getEmploymentStatus()

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
                employeeDto.getEmploymentStatus()

        );
    }
}
