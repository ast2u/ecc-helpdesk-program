package com.carloprogram.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.TicketRemarks;
import com.carloprogram.specification.TicketSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private HelpTicketRepository helpTicketRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private HelpTicketMapper ticketMapper;

    @LogExecution
    @Transactional
    @Override
    public HelpTicketDto createTicket(HelpTicketDto helpTicketDto, Long createdById) {
        Employee createdBy = employeeRepository.findById(createdById)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        HelpTicket ticket = ticketMapper.mapToTicket(helpTicketDto);
        ticket.setAssignee(null);
        ticket.setCreatedBy(createdBy);
        ticket.setUpdatedBy(createdBy);
        ticket.setRemarks(null);

        if(ticket.getStatus() == null ){
            ticket.setStatus(TicketStatus.DRAFT);
        }

        ticket = helpTicketRepository.save(ticket);

        return ticketMapper.mapToTicketDto(ticket);
    }

    @LogExecution
    @Override
    public HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto, Long updatedById) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee updatedBy = employeeRepository.findById(updatedById)
                .orElseThrow(() -> new RuntimeException("Updater not found"));

        ticket.setTitle(helpTicketDto.getTitle());
        ticket.setBody(helpTicketDto.getBody());
        ticket.setStatus(helpTicketDto.getStatus());
        ticket.setUpdatedBy(updatedBy);

        ticket = helpTicketRepository.save(ticket);
        return ticketMapper.mapToTicketDto(ticket);
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

        return ticketMapper.mapToTicketDto(ticket);
    }

    @Override
    public Page<HelpTicketDto> getAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<HelpTicket> ticketPage = helpTicketRepository.findAll(pageable);

        return ticketPage.map(ticketMapper::mapToTicketDto);
    }

    public Page<HelpTicketDto> searchTickets(TicketSearchRequest ticketSearch){
        Specification<HelpTicket> spec = Specification.where(null);

        if (ticketSearch.getStatus() != null) {
            spec = spec.and(TicketSpecification.hasStatus(TicketStatus.valueOf(ticketSearch.getStatus())));
        }

        if (ticketSearch.getCreatedBy() != null && ticketSearch.getCreatedBy() > 0) {
            spec = spec.and(TicketSpecification.createdBy(ticketSearch.getCreatedBy()));
        }
        if (ticketSearch.getUpdatedBy() != null && ticketSearch.getUpdatedBy() > 0) {
            spec = spec.and(TicketSpecification.updatedBy(ticketSearch.getUpdatedBy()));
        }
        if (ticketSearch.getAssignee() != null && ticketSearch.getAssignee() > 0) {
            spec = spec.and(TicketSpecification.assignedTo(ticketSearch.getAssignee()));
        }
        if (ticketSearch.getCreatedStart() != null && ticketSearch.getCreatedEnd() != null) {
            spec = spec.and(TicketSpecification.createdBetween(ticketSearch.getCreatedStart(), ticketSearch.getCreatedEnd()));
        }
        if (ticketSearch.getUpdatedStart() != null && ticketSearch.getUpdatedEnd() != null) {
            spec = spec.and(TicketSpecification.updatedBetween(ticketSearch.getUpdatedStart(), ticketSearch.getUpdatedEnd()));
        }

        Pageable pageable = PageRequest.of(ticketSearch.getPage(), ticketSearch.getSize());

        Page<HelpTicket> ticketPage = helpTicketRepository.findAll(spec, pageable);

        return ticketPage.map(ticketMapper::mapToTicketDto);
    }


    @Override
    public HelpTicketDto getTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket id does not exists " +
                        "with given ticket number: "+ id));

        return ticketMapper.mapToTicketDto(ticket);

    }

    //Can only be deleted by an employee (to clarify can be deleted by admin)
    // Removed?
    @Override
    public void deleteTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket does not exists " +
                                "with given Ticket Number: "+ id));
        helpTicketRepository.delete(ticket);
    }
}
