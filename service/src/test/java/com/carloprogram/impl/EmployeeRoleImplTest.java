package com.carloprogram.impl;


import com.carloprogram.dto.EmployeeRoleDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.model.EmployeeRole;
import com.carloprogram.repository.EmployeeRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeRoleImplTest {

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @InjectMocks
    private EmployeeRoleServiceImpl employeeRoleService;

    private EmployeeRole employeeRole;
    private EmployeeRoleDto employeeRoleDto;

    @BeforeEach
    public void setUp() {
        employeeRole = new EmployeeRole();
        employeeRole.setId(1L);
        employeeRole.setRole_title("Software Engineer");
        employeeRole.setRole_description("Develops software applications");

        employeeRoleDto = new EmployeeRoleDto();
        employeeRoleDto.setId(1L);
        employeeRoleDto.setRole_title("Software Engineer");
        employeeRoleDto.setRole_description("Develops software applications");

    }

    @Test
    public void testCreateEmployeeRole() {
        when(employeeRoleRepository.save(any(EmployeeRole.class))).thenReturn(employeeRole);

        EmployeeRoleDto result = employeeRoleService.createEmployeeRole(employeeRoleDto);

        assertNotNull(result);
        assertEquals(employeeRoleDto.getRole_title(), result.getRole_title());
        assertEquals(employeeRoleDto.getRole_description(), result.getRole_description());

        verify(employeeRoleRepository, times(1)).save(any(EmployeeRole.class));
    }

    @Test
    public void testGetEmployeeRoleById_Exists() {
        when(employeeRoleRepository.findById(1L)).thenReturn(Optional.of(employeeRole));

        EmployeeRoleDto result = employeeRoleService.getEmployeeRoleById(1L);

        assertNotNull(result);
        assertEquals("Software Engineer", result.getRole_title());

        verify(employeeRoleRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployeeRoleById_NotFound() {
        when(employeeRoleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeRoleService.getEmployeeRoleById(1L));

        verify(employeeRoleRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllEmployeeRoles() {
        List<EmployeeRole> employeeRoles = Collections.singletonList(employeeRole);
        when(employeeRoleRepository.findAll()).thenReturn(employeeRoles);

        List<EmployeeRoleDto> result = employeeRoleService.getAllEmployeeRoles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Software Engineer", result.get(0).getRole_title());

        verify(employeeRoleRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateEmployeeRoleById() {
        EmployeeRoleDto updatedDto = new EmployeeRoleDto();
        updatedDto.setRole_title("Senior Engineer");
        updatedDto.setRole_description("Leads software development");

        when(employeeRoleRepository.findById(1L)).thenReturn(Optional.of(employeeRole));
        when(employeeRoleRepository.save(any(EmployeeRole.class))).thenReturn(employeeRole);

        EmployeeRoleDto result = employeeRoleService.updateEmployeeRoleById(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Senior Engineer", result.getRole_title());
        assertEquals("Leads software development", result.getRole_description());

        verify(employeeRoleRepository, times(1)).save(any(EmployeeRole.class));
    }

    @Test
    public void testUpdateEmployeeRoleById_RoleNotExist() {
        EmployeeRoleDto updatedDto = new EmployeeRoleDto();
        updatedDto.setRole_title("Senior Engineer");
        updatedDto.setRole_description("Leads software development");

        when(employeeRoleRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeRoleService.updateEmployeeRoleById(20L, updatedDto));

        verify(employeeRoleRepository, times(1)).findById(20L);
    }

    @Test
    public void testDeleteEmployeeRoleById_Exists() {
        when(employeeRoleRepository.findById(1L)).thenReturn(Optional.of(employeeRole));
        doNothing().when(employeeRoleRepository).deleteById(1L);

        assertDoesNotThrow(() -> employeeRoleService.deleteEmployeeRoleById(1L));

        verify(employeeRoleRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEmployeeRoleById_NotFound() {
        when(employeeRoleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeRoleService.deleteEmployeeRoleById(1L));

        verify(employeeRoleRepository, times(1)).findById(1L);
    }
}

