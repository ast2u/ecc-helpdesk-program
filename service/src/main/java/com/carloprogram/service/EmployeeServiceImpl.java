package com.carloprogram.service;

import com.carloprogram.mapper.EmployeeMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.dto.EmployeeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);


        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }
}
