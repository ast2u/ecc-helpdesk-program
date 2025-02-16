package com.carloprogram.impl;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.TicketRemarks;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.repository.TicketRemarksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketRemarksImplTest {

    @Mock
    private TicketRemarksRepository ticketRemarksRepository;

    @Mock
    private HelpTicketRepository helpTicketRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TicketRemarksImpl ticketImpl;

    private HelpTicket testTicket;
    private TicketRemarks ticketRemarks;
    private TicketRemarksDto testTicketRemarksDto;
    private Employee testEmployee;

    @BeforeEach
    public void setUp(){
        testEmployee = new Employee();
        testEmployee.setId(5L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");

        testTicket = new HelpTicket();
        testTicket.setTicketNumber(10L);
        testTicket.setTicketTitle("Test Ticket");
        testTicket.setBody("This is a test ticket.");
        testTicket.setStatus(TicketStatus.DRAFT);
        testTicket.setCreatedBy(testEmployee);
        testTicket.setCreatedDate(LocalDateTime.now());

        ticketRemarks = new TicketRemarks();
        ticketRemarks.setId(1L);
        ticketRemarks.setTicketNumber(testTicket);
        ticketRemarks.setEmployeeId(testEmployee);
        ticketRemarks.setCreatedDate(LocalDateTime.now());
        ticketRemarks.setComment("Testing comment/remarks");

        testTicketRemarksDto = new TicketRemarksDto(
                ticketRemarks.getId(),
                ticketRemarks.getTicketNumber().getTicketNumber(),
                ticketRemarks.getEmployeeId().getId(),
                ticketRemarks.getComment(),
                ticketRemarks.getCreatedDate()
        );

        testTicket.setRemarks(List.of(ticketRemarks));

    }

    @Test
    public void testAddRemark() {
        when(helpTicketRepository.findById(10L)).thenReturn(Optional.of(testTicket));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(testEmployee));

        when(ticketRemarksRepository.save(any(TicketRemarks.class))).thenReturn(ticketRemarks);

        TicketRemarksDto result = ticketImpl.addRemark(testTicketRemarksDto,10L,5L);

        assertNotNull(result);
        assertEquals("Testing comment/remarks",result.getComment());
        verify(ticketRemarksRepository, times(1)).save(any(TicketRemarks.class));
    }

    @Test
    public void testAddRemark_TicketNotFound() {
        when(helpTicketRepository.findById(10L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () ->
                ticketImpl.addRemark(testTicketRemarksDto,10L,1L));
        assertEquals("Ticket not found with ticketNumber: 10", exception.getMessage());
    }

    @Test
    public void testAddRemark_EmployeeNotFound() {
        when(helpTicketRepository.findById(10L)).thenReturn(Optional.of(testTicket));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                ticketImpl.addRemark(testTicketRemarksDto,10L,1L));
        assertEquals("Employee not found with ID: 1", exception.getMessage());
    }

    @Test
    public void testGetRemarksByTicketId(){
        when(helpTicketRepository.findById(10L)).thenReturn(Optional.of(testTicket));
        when(ticketRemarksRepository.findByTicketNumber(testTicket)).thenReturn(List.of(ticketRemarks));

        List<TicketRemarksDto> result = ticketImpl.getRemarksByTicketId(10L);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("Testing comment/remarks", result.getFirst().getComment());

        verify(helpTicketRepository, times(1)).findById(10L);
        verify(ticketRemarksRepository, times(1)).findByTicketNumber(testTicket);

    }

    @Test
    public void testGetRemarksByTicketId_TicketNotFound() {
        when(helpTicketRepository.findById(15L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                ticketImpl.getRemarksByTicketId(15L));

        assertEquals("Ticket not found with ticketNumber: 15", exception.getMessage());

        verify(helpTicketRepository, times(1)).findById(15L);
        verify(ticketRemarksRepository, never()).findByTicketNumber(any());
    }


    @Test
    public void testDeleteRemark() {
        when(helpTicketRepository.findById(10L)).thenReturn(Optional.of(testTicket));
        when(ticketRemarksRepository.findById(1L)).thenReturn(Optional.of(ticketRemarks));

        ticketImpl.deleteRemark(10L, 1L);

        verify(ticketRemarksRepository, times(1)).delete(ticketRemarks);
    }

    @Test
    public void testDeleteRemark_TicketNotFound() {
        when(helpTicketRepository.findById(15L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                ticketImpl.deleteRemark(15L,1L));
        assertEquals("Ticket not found with ticketNumber: 15", exception.getMessage());
    }

    @Test
    public void testDeleteRemark_RemarkNotExist() {
        when(helpTicketRepository.findById(10L)).thenReturn(Optional.of(testTicket));
        when(ticketRemarksRepository.findById(10L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                ticketImpl.deleteRemark(10L,10L));
        assertEquals("This remark does not exist", exception.getMessage());
    }

}
