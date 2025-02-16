package com.carloprogram.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.mapper.EmployeeMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.EmploymentStatus;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private HelpTicketRepository helpTicketRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private HelpTicket testTicket;
    private HelpTicketDto testTicketDto;
    private Employee testEmployee;

    @BeforeEach
    void setUp(){
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");

        testTicket = new HelpTicket();
        testTicket.setTicketNumber(100L);
        testTicket.setTicketTitle("Test Ticket");
        testTicket.setBody("This is a test ticket.");
        testTicket.setStatus(null);
        testTicket.setCreatedBy(testEmployee);
        testTicket.setCreatedDate(LocalDateTime.now());

        testTicketDto = new HelpTicketDto(
                testTicket.getTicketNumber(),
                testTicket.getTicketTitle(),
                testTicket.getBody(),
                testTicket.getStatus(),
                testTicket.getCreatedDate(),
                testTicket.getUpdatedDate(),
                null, // Assignee
                EmployeeMapper.mapToEmployeeDto(testEmployee),
                null, // UpdatedBy
                List.of() // Remarks
        );
    }

    @Test
    public void testCreateTicket_SuccessAndStatusNull() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(helpTicketRepository.save(any(HelpTicket.class))).thenAnswer(invocation -> {
            HelpTicket savedHelpTicket = invocation.getArgument(0);
            if (savedHelpTicket.getStatus() == null) {
                savedHelpTicket.setStatus(TicketStatus.DRAFT);
            }
            return savedHelpTicket;
        });

        HelpTicketDto result = ticketService.createTicket(testTicketDto, 1L);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
        assertEquals(TicketStatus.DRAFT,result.getStatus());
        verify(helpTicketRepository, times(1)).save(any(HelpTicket.class));
    }

    @Test
    public void testCreateTicket_Fail_EmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                ticketService.createTicket(testTicketDto, 1L));

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void testUpdateTicket_Success() {
        HelpTicketDto updatedDto = new HelpTicketDto(
                testTicket.getTicketNumber(),
                "Updated Ticket Title",
                "Updated Body",
                TicketStatus.FILED,
                null, null, null, null, null, List.of()
        );

        when(helpTicketRepository.findById(100L)).thenReturn(Optional.of(testTicket));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(helpTicketRepository.save(any(HelpTicket.class))).thenReturn(testTicket);

        HelpTicketDto result = ticketService.updateTicket(100L, updatedDto, 1L);

        assertNotNull(result);
        assertEquals("Updated Ticket Title", result.getTitle());
        assertEquals("Updated Body", result.getBody());
        assertEquals(TicketStatus.FILED, result.getStatus());

        verify(helpTicketRepository, times(1)).findById(100L);
        verify(employeeRepository, times(1)).findById(1L);
        verify(helpTicketRepository, times(1)).save(any(HelpTicket.class));
    }

    @Test
    void testUpdateTicket_Fail_TicketNotFound() {
        when(helpTicketRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.updateTicket(999L, testTicketDto, 1L);
        });

        assertEquals("Ticket not found", exception.getMessage());
    }

    @Test
    void testUpdateTicket_Fail_UpdaterNotFound() {
        when(helpTicketRepository.findById(100L)).thenReturn(Optional.of(testTicket));
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.updateTicket(100L, testTicketDto, 999L);
        });

        assertEquals("Updater not found", exception.getMessage());
    }

    @Test
    public void testGetTicketById_Success() {
        when(helpTicketRepository.findById(100L)).thenReturn(Optional.of(testTicket));

        HelpTicketDto result = ticketService.getTicketById(100L);

        assertNotNull(result);
        assertEquals("Test Ticket", result.getTitle());
    }

    @Test
    public void testGetTicketById_Fail_TicketNotFound() {
        when(helpTicketRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.getTicketById(999L);
        });

        assertEquals("Ticket Number does not exists with given ticket number: 999", exception.getMessage());
    }

    @Test
    public void testGetAllTickets(){
        when(helpTicketRepository.findAll()).thenReturn(Collections.singletonList(testTicket));

        List<HelpTicketDto> result = ticketService.getAllTickets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Ticket", result.getFirst().getTitle());

        // Verify repository interaction
        verify(helpTicketRepository, times(1)).findAll();
    }

    @Test
    public void testAssignTicket_Success() {
        Employee assignee = new Employee();
        assignee.setId(2L);
        assignee.setFirstName("Jane");
        assignee.setLastName("Doe");

        when(helpTicketRepository.findById(100L)).thenReturn(Optional.of(testTicket));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(helpTicketRepository.save(any(HelpTicket.class))).thenReturn(testTicket);

        HelpTicketDto result = ticketService.assignTicket(100L, 2L);

        assertNotNull(result);
        assertEquals("Jane", result.getAssigneeId().getFirstName());
    }

    @Test
    public void testAssignTicket_TicketNotFound(){
        when(helpTicketRepository.findById(999L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.assignTicket(999L,1L);
        });
        assertEquals("Ticket not found",exception.getMessage());
    }

    @Test
    public void testAssignTicket_AssigneeNotFound(){

        when(helpTicketRepository.findById(100L)).thenReturn(Optional.of(testTicket));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.assignTicket(100L, 999L);
        });
        assertEquals("Assignee not found",exception.getMessage());
    }

    @Test
    public void testDeleteTicketById_Success() {
        when(helpTicketRepository.findById(100L)).thenReturn(Optional.of(testTicket));

        ticketService.deleteTicketById(100L);

        verify(helpTicketRepository, times(1)).delete(testTicket);
    }

    @Test
    void testDeleteTicketById_Fail_TicketNotFound() {
        when(helpTicketRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.deleteTicketById(999L);
        });

        assertEquals("Ticket does not exists with given Ticket Number: 999", exception.getMessage());
    }

}
