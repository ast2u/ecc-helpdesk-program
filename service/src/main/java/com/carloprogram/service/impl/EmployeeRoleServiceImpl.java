package com.carloprogram.service.impl;

import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.EmployeeRoleMapper;
import com.carloprogram.model.EmployeeRole;
import com.carloprogram.repository.EmployeeRoleRepository;
import com.carloprogram.service.EmployeeRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeRoleServiceImpl implements EmployeeRoleService {

    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeRoleMapper employeeRoleMapper;

    public EmployeeRoleServiceImpl(EmployeeRoleRepository employeeRoleRepository,
                                   EmployeeRoleMapper employeeRoleMapper){
        this.employeeRoleRepository = employeeRoleRepository;
        this.employeeRoleMapper = employeeRoleMapper;
    }

    @Transactional
    @LogExecution
    @Override
    public EmployeeRoleDto createEmployeeRole(EmployeeRoleDto employeeRoleDto) {
        EmployeeRole employeeRole = employeeRoleMapper.mapToEmployeeRole(employeeRoleDto);
        EmployeeRole savedEmployeeRole = employeeRoleRepository.save(employeeRole);
        return employeeRoleMapper.mapToEmployeeRoleDto(savedEmployeeRole);

    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeRoleDto getEmployeeRoleById(Long employeeRoleId) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(employeeRoleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists " +
                                "with given id: " + employeeRoleId));

        return employeeRoleMapper.mapToEmployeeRoleDto(employeeRole);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeRoleDto> getAllEmployeeRoles() {
        List<EmployeeRole> employeeRoles = employeeRoleRepository.findAll();

        return employeeRoles.stream()
                .map(employeeRoleMapper::mapToEmployeeRoleDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @LogExecution
    @Override
    public EmployeeRoleDto updateEmployeeRoleById(Long employeeRoleId, EmployeeRoleDto updatedEmployeeRole) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(employeeRoleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role does not exist " +
                                "with given id: " + employeeRoleId));
        employeeRole.setRole_title(updatedEmployeeRole.getRole_title());
        employeeRole.setRole_description(updatedEmployeeRole.getRole_description());
        EmployeeRole updatedEmployeeRoleObj = employeeRoleRepository.save(employeeRole);
        return employeeRoleMapper.mapToEmployeeRoleDto(updatedEmployeeRoleObj);
    }

    @Transactional
    @Override
    public void deleteEmployeeRoleById(Long employeeRoleId) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(employeeRoleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role does not exist " +
                                "with given id: " + employeeRoleId));

        employeeRoleRepository.delete(employeeRole);
    }
}
