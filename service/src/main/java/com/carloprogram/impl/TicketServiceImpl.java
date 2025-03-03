package com.carloprogram.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.exception.UnauthorizedException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.specification.TicketSpecification;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.service.TicketService;
import com.carloprogram.util.HelpTicketUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public HelpTicketDto createTicket(HelpTicketDto helpTicketDto, EmployeeUserPrincipal employeeUserPrincipal) {
        Employee createdBy = employeeRepository.findById(employeeUserPrincipal.getEmployee().getId())
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
    @Transactional
    @Override
    public HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto, EmployeeUserPrincipal userPrincipal) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee currentUser = employeeRepository.findById(userPrincipal.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Updater not found"));

        if(HelpTicketUtil.canUpdateTicket(currentUser, ticket)){
            throw new UnauthorizedException("You are not authorized to update this ticket");
        }

        HelpTicketUtil.updateTicketFields(ticket, helpTicketDto);
        ticket.setUpdatedBy(currentUser);

        ticket = helpTicketRepository.save(ticket);
        return ticketMapper.mapToTicketDto(ticket);
    }

    @LogExecution
    @Transactional
    @Override
    public HelpTicketDto assignTicket(Long id, Long assigneeId, EmployeeUserPrincipal userPrincipal) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee assignee = employeeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        Employee currentUser = employeeRepository.findById(userPrincipal.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Updater not found"));

        if(HelpTicketUtil.canUpdateTicket(currentUser, ticket)){
            throw new UnauthorizedException("You are not authorized to update this ticket");
        }

        ticket.setAssignee(assignee);
        ticket.setUpdatedBy(currentUser);

        ticket = helpTicketRepository.save(ticket);

        return ticketMapper.mapToTicketDto(ticket);
    }


    @Transactional(readOnly = true)
    @Override
    public Page<HelpTicketDto> getAllFilterTickets(TicketSearchRequest ticketSearch){
        Specification<HelpTicket> spec = TicketSpecification.filterTickets(ticketSearch);

        Pageable pageable = PageRequest.of(ticketSearch.getPage(), ticketSearch.getSize());

        Page<HelpTicket> ticketPage = helpTicketRepository.findAll(spec, pageable);

        return ticketPage.map(ticketMapper::mapToTicketDto);
    }

    @Transactional(readOnly = true)
    @Override
    public HelpTicketDto getTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket id does not exists " +
                        "with given ticket number: "+ id));

        return ticketMapper.mapToTicketDto(ticket);

    }

    //Can only be deleted by an employee (to clarify can be deleted by admin)
    // Removed?
    @Transactional
    @Override
    public void deleteTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket does not exists " +
                                "with given Ticket Number: "+ id));
        helpTicketRepository.delete(ticket);
    }
}
