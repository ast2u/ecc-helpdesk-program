package com.carloprogram.impl;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeRole;
import com.carloprogram.model.enums.EmploymentStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.EmployeeRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee testEmployee;
    private EmployeeRole testRole;
    private EmployeeDto testEmployeeDto;

    @BeforeEach
    public void setUp() {
        testRole = new EmployeeRole();
        testRole.setId(1L);
        testRole.setRole_title("Software Engineer");



        testEmployee = new Employee();
        testEmployee.setId(5L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setAge(30);
        testEmployee.setAddress("123 Street");
        testEmployee.setContactNumber("123-456-7890");
        testEmployee.setEmploymentStatus(EmploymentStatus.INTERN);
        testEmployee.setEmployeeRoles(List.of(testRole));

        testEmployeeDto = new EmployeeDto(
                testEmployee.getId(),
                testEmployee.getFirstName(),
                testEmployee.getLastName(),
                testEmployee.getAge(),
                testEmployee.getAddress(),
                testEmployee.getContactNumber(),
                testEmployee.getEmploymentStatus(),
                List.of() // Employee Role DTOs
        );
    }

    @Test
    public void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeDto result = employeeService.createEmployee(testEmployeeDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(EmploymentStatus.INTERN, result.getEmploymentStatus());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployee_WithNullRolesAndNullEmploymentStatus() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setAge(25);
        employeeDto.setAddress("123 Main St");
        employeeDto.setContactNumber("123-456-7890");
        employeeDto.setEmployeeRoleIds(null);
        employeeDto.setEmploymentStatus(null);

        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setAge(employeeDto.getAge());
        employee.setAddress(employeeDto.getAddress());
        employee.setContactNumber(employeeDto.getContactNumber());
        employee.setEmploymentStatus(null);
        employee.setEmployeeRoles(null);

        // Mock Repository Behavior
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee savedEmployee = invocation.getArgument(0);
            if (savedEmployee.getEmploymentStatus() == null) {
                savedEmployee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
            }
            return savedEmployee;
        });

        EmployeeDto result = employeeService.createEmployee(employeeDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertNull(result.getEmployeeRoleIds());
        assertEquals(EmploymentStatus.FULL_TIME, result.getEmploymentStatus());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }


    @Test
    public void testGetEmployeeById() {
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(testEmployee));

        EmployeeDto result = employeeService.getEmployeeById(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("John", result.getFirstName());

        verify(employeeRepository, times(1)).findById(5L);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(20L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                employeeService.getEmployeeById(20L));

        assertEquals("Employee does not exists with given id: 20", exception.getMessage());

        verify(employeeRepository, times(1)).findById(20L);
    }

    @Test
    public void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(testEmployee));

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateEmployeeById() {
        when(employeeRepository.findById(100L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeDto updatedDto = new EmployeeDto(
                100L, "Jane", "Doe", 28, "456 Main St",
                "987-654-3210", EmploymentStatus.FULL_TIME, List.of());

        EmployeeDto result = employeeService.updateEmployeeById(100L, updatedDto);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals(28, result.getAge());
        assertEquals("456 Main St", result.getAddress());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeById_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        EmployeeDto updatedDto = new EmployeeDto(999L, "Jane", "Doe", 28, "456 Main St",
                "987-654-3210", EmploymentStatus.INTERN, List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                employeeService.updateEmployeeById(999L, updatedDto));

        assertEquals("Employee does not exists with given id: 999", exception.getMessage());

        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    public void testAssignRoleToEmployee() {

        List<Long> roleIds = Arrays.asList(1L, 2L);

        EmployeeRole testRole2 = new EmployeeRole();
        testRole2.setId(2L);
        testRole2.setRole_title("Manager");

        Employee employee = new Employee();
        employee.setId(10L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setAge(30);
        employee.setAddress("123 Street");
        employee.setContactNumber("123-456-7890");
        employee.setEmploymentStatus(EmploymentStatus.INTERN);
        employee.setEmployeeRoles(null);

        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));
        when(employeeRoleRepository.findById(1L)).thenReturn(Optional.of(testRole));
        when(employeeRoleRepository.findById(2L)).thenReturn(Optional.of(testRole2));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto updatedEmployee = employeeService.assignRoleToEmployee(10L, roleIds);

        assertNotNull(updatedEmployee);
        assertEquals(2, updatedEmployee.getEmployeeRoleIds().size());
        assertEquals("Software Engineer", updatedEmployee.getEmployeeRoleIds().get(0).getRole_title());
        assertEquals("Manager", updatedEmployee.getEmployeeRoleIds().get(1).getRole_title());

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    public void testDeleteEmployeeById() {
        when(employeeRepository.findById(100L)).thenReturn(Optional.of(testEmployee));

        employeeService.deleteEmployeeById(100L);

        verify(employeeRepository, times(1)).delete(testEmployee);
    }

    @Test
    public void testDeleteEmployeeById_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                employeeService.deleteEmployeeById(999L));

        assertEquals("Employee does not exists with given id: 999", exception.getMessage());

        verify(employeeRepository, times(1)).findById(999L);
    }
}
