package com.carloprogram.impl;

import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.mapper.EmployeeMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeRole;
import com.carloprogram.model.enums.EmploymentStatus;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.repository.EmployeeRoleRepository;
import com.carloprogram.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmployeeRoleRepository employeeRoleRepository;

    @Transactional
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        if (employeeDto.getEmployeeRoleIds() != null) {
            List<EmployeeRole> roles = employeeDto.getEmployeeRoleIds()
                    .stream()
                    .map(roleId -> employeeRoleRepository.findById(roleId.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            employee.setEmployeeRoles(roles);
        } else {
            employee.setEmployeeRoles(null);
        }

        if (employee.getEmploymentStatus() == null) {
            employee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists " +
                                "with given id: " + employeeId));

        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists" +
                                "with given id: "+ employeeId));

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setAge(updatedEmployee.getAge());
        employee.setAddress(updatedEmployee.getAddress());
        employee.setContactNumber(updatedEmployee.getContactNumber());
        employee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());

        if(employee.getEmployeeRoles() != null){
            List<EmployeeRole> roles = updatedEmployee.getEmployeeRoleIds().stream()
                    .map(roleId -> employeeRoleRepository.findById(roleId.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            employee.setEmployeeRoles(roles);
        }else {
            employee.setEmployeeRoles(null);
        }

        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Transactional
    @Override
    public void deleteEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists" +
                                "with given id: "+ employeeId));
        employeeRepository.delete(employee);
    }

}
