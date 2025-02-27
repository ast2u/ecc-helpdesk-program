package com.carloprogram.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.service.TicketService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private HelpTicketRepository helpTicketRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LogExecution
    @Transactional
    @Override
    public HelpTicketDto createTicket(HelpTicketDto helpTicketDto, Long createdById) {
        Employee createdBy = employeeRepository.findById(createdById)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        HelpTicket ticket = HelpTicketMapper.mapToTicket(helpTicketDto, null, createdBy, null,null);

        if(ticket.getStatus() == null ){
            ticket.setStatus(TicketStatus.DRAFT);
        }

        ticket = helpTicketRepository.save(ticket);

        return HelpTicketMapper.mapToTicketDto(ticket);
    }

    @LogExecution
    @Override
    public HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto, Long updatedById) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee updatedBy = employeeRepository.findById(updatedById)
                .orElseThrow(() -> new RuntimeException("Updater not found"));

        ticket.setTicketTitle(helpTicketDto.getTitle());
        ticket.setBody(helpTicketDto.getBody());
        ticket.setStatus(helpTicketDto.getStatus());
        ticket.setUpdatedBy(updatedBy);

        ticket = helpTicketRepository.save(ticket);
        return HelpTicketMapper.mapToTicketDto(ticket);
    }

    @LogExecution
    @Override
    public HelpTicketDto assignTicket(Long id, Long assigneeId) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee assignee = employeeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        ticket.setAssignee(assignee);
        ticket = helpTicketRepository.save(ticket);

        return HelpTicketMapper.mapToTicketDto(ticket);
    }

    @Override
    public Page<HelpTicketDto> getAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<HelpTicket> ticketPage = helpTicketRepository.findAll(pageable);

        return ticketPage.map(HelpTicketMapper::mapToTicketDto);
    }


    @Override
    public HelpTicketDto getTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket id does not exists " +
                        "with given ticket number: "+ id));

        return HelpTicketMapper.mapToTicketDto(ticket);

    }

    //Can only be deleted by an employee (to clarify can be deleted by admin)
    @Override
    public void deleteTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket does not exists " +
                                "with given Ticket Number: "+ id));
        helpTicketRepository.delete(ticket);
    }
}
