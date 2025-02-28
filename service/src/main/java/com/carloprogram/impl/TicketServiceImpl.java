package com.carloprogram.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
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

import java.time.LocalDateTime;
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

    public Page<HelpTicket> searchTickets(Integer page, Integer size,
                                          String status, Long createdBy, Long updatedBy,
                                          Long assignee, LocalDateTime createdStart, LocalDateTime createdEnd,
                                          LocalDateTime updatedStart, LocalDateTime updatedEnd){
        Specification<HelpTicket> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and(TicketSpecification.hasStatus(TicketStatus.valueOf(status)));
        }

        if (createdBy != null && createdBy > 0) {
            spec = spec.and(TicketSpecification.createdBy(createdBy));
        }
        if (updatedBy != null && updatedBy > 0) {
            spec = spec.and(TicketSpecification.updatedBy(updatedBy));
        }
        if (assignee != null && assignee > 0) {
            spec = spec.and(TicketSpecification.assignedTo(assignee));
        }
        if (createdStart != null && createdEnd != null) {
            spec = spec.and(TicketSpecification.createdBetween(createdStart, createdEnd));
        }
        if (updatedStart != null && updatedEnd != null) {
            spec = spec.and(TicketSpecification.updatedBetween(updatedStart, updatedEnd));
        }

        Pageable pageable = PageRequest.of(page, size);

        return helpTicketRepository.findAll(spec, pageable);
    }


    @Override
    public HelpTicketDto getTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket id does not exists " +
                        "with given ticket number: "+ id));

        return HelpTicketMapper.mapToTicketDto(ticket);

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
