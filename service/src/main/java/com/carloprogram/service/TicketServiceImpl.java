package com.carloprogram.service;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.mapper.EmployeeMapper;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final HelpTicketRepository helpTicketRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public HelpTicketDto createTicket(HelpTicketDto helpTicketDto, Long createdById) {
        Employee createdBy = employeeRepository.findById(createdById)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        HelpTicket ticket = HelpTicketMapper.mapToTicket(helpTicketDto, null, createdBy, null);
        ticket.setStatus(TicketStatus.DRAFT); // Default status on creation
        ticket = helpTicketRepository.save(ticket);

        return HelpTicketMapper.mapToTicketDto(ticket);
    }

    @Override
    public HelpTicketDto updateTicket(Long ticketNumber, HelpTicketDto helpTicketDto, Long updatedById) {
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee updatedBy = employeeRepository.findById(updatedById)
                .orElseThrow(() -> new RuntimeException("Updater not found"));

        ticket.setTicketTitle(helpTicketDto.getTitle());
        ticket.setBody(helpTicketDto.getBody());
        ticket.setStatus(helpTicketDto.getStatus());
        ticket.setRemarks(helpTicketDto.getRemarks());
        ticket.setUpdatedBy(updatedBy);

        ticket = helpTicketRepository.save(ticket);
        return HelpTicketMapper.mapToTicketDto(ticket);
    }

    @Override
    public HelpTicketDto assignTicket(Long ticketNumber, Long assigneeId) {
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee assignee = employeeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        ticket.setAssignee(assignee);
        ticket = helpTicketRepository.save(ticket);

        return HelpTicketMapper.mapToTicketDto(ticket);
    }

    @Override
    public List<HelpTicketDto> getAllTickets() {
        return helpTicketRepository.findAll().stream()
                .map(HelpTicketMapper::mapToTicketDto)
                .collect(Collectors.toList());
    }


    @Override
    public HelpTicketDto getTicketById(Long ticketNumber) {
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket Number does not exists " +
                        "with given ticket number: "+ticketNumber));

        return HelpTicketMapper.mapToTicketDto(ticket);

    }

    //Can only be deleted by an employee (to clarify can be deleted by admin)
    @Override
    public void deleteTicketById(Long ticketNumber) {
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket does not exists" +
                                "with given Ticket Number: "+ ticketNumber));
        helpTicketRepository.delete(ticket);
    }
}
