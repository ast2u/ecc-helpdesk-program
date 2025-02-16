package com.carloprogram.impl;

import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.mapper.EmployeeRoleMapper;
import com.carloprogram.model.EmployeeRole;
import com.carloprogram.repository.EmployeeRoleRepository;
import com.carloprogram.service.EmployeeRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeRoleServiceImpl implements EmployeeRoleService {
    private EmployeeRoleRepository employeeRoleRepository;

    @Transactional
    @Override
    public EmployeeRoleDto createEmployeeRole(EmployeeRoleDto employeeRoleDto) {
        EmployeeRole employeeRole = EmployeeRoleMapper.mapToEmployeeRole(employeeRoleDto);
        EmployeeRole savedEmployeeRole = employeeRoleRepository.save(employeeRole);
        return EmployeeRoleMapper.mapToEmployeeRoleDto(savedEmployeeRole);

    }

    @Override
    public EmployeeRoleDto getEmployeeRoleById(Long employeeRoleId) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(employeeRoleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists " +
                                "with given id: " + employeeRoleId));

        return EmployeeRoleMapper.mapToEmployeeRoleDto(employeeRole);
    }

    @Override
    public List<EmployeeRoleDto> getAllEmployeeRoles() {
        List<EmployeeRole> employeeRoles = employeeRoleRepository.findAll();

        return employeeRoles.stream()
                .map(EmployeeRoleMapper::mapToEmployeeRoleDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EmployeeRoleDto updateEmployeeRoleById(Long employeeRoleId, EmployeeRoleDto updatedEmployeeRole) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(employeeRoleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role does not exist " +
                                "with given id: " + employeeRoleId));
        employeeRole.setRole_title(updatedEmployeeRole.getRole_title());
        employeeRole.setRole_description(updatedEmployeeRole.getRole_description());
        EmployeeRole updatedEmployeeRoleObj = employeeRoleRepository.save(employeeRole);
        return EmployeeRoleMapper.mapToEmployeeRoleDto(updatedEmployeeRoleObj);
    }

    @Transactional
    @Override
    public void deleteEmployeeRoleById(Long employeeRoleId) {
        EmployeeRole employeeRole = employeeRoleRepository.findById(employeeRoleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role does not exist " +
                                "with given id: " + employeeRoleId));

        employeeRoleRepository.deleteById(employeeRoleId);
    }
}
