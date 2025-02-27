package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.impl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@Disabled("Temporarily disabling this test class")
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;

    private EmployeeDto testEmployeeDto;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
        testEmployeeDto = new EmployeeDto(1L, "John", "Doe",
                25, "123 Main St", "1234567890",
                null, null);
    }


    @Test
    public void testCreateEmployee() throws Exception{
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(testEmployeeDto);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEmployeeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployeeDto);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

//    @Test
//    void testGetAllEmployees() throws Exception {
//        List<EmployeeDto> employees = Arrays.asList(
//                new EmployeeDto(1L, "Alice", "Brown",
//                        28, "789 Pine St", "1112223333",
//                        null, null),
//
//                new EmployeeDto(2L, "Bob", "White",
//                        35, "101 Maple St", null,
//                        null, null)
//        );
//        when(employeeService.getAllEmployees()).thenReturn(employees);
//
//        mockMvc.perform(get("/api/employees"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].firstName").value("Alice"))
//                .andExpect(jsonPath("$[1].firstName").value("Bob"));
//    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeDto updatedEmployee = new EmployeeDto(1L, "Updated", "Name",
                29, "Updated Address", null,
                null, null);

        when(employeeService.updateEmployeeById(eq(1L),
                any(EmployeeDto.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    void testAssignRolesToEmployee() throws Exception {
        EmployeeDto employeeWithRoles = new EmployeeDto(1L, "John", "Doe",
                25, "123 Main St", "1234567890",
                null, null);
        when(employeeService.assignRoleToEmployee(eq(1L), eq(Arrays.asList(1L, 2L))))
                .thenReturn(employeeWithRoles);

        mockMvc.perform(put("/api/employees/1/assign-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(1L, 2L))))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());
    }

}
