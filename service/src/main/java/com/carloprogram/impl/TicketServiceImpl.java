package com.carloprogram.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.exception.UnauthorizedException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.security.config.SecurityUtil;
import com.carloprogram.specification.TicketSpecification;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.service.TicketService;
import com.carloprogram.util.ticket.HelpTicketUtil;
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

    @Autowired
    private SecurityUtil securityUtil;

    @LogExecution
    @Transactional
    @Override
    public HelpTicketDto createTicket(HelpTicketDto helpTicketDto) {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        HelpTicket ticket = ticketMapper.mapToTicket(helpTicketDto);
        ticket.setAssignee(null);
        ticket.setCreatedBy(currentUser.getUsername());
        ticket.setUpdatedBy(currentUser.getUsername());
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
    public HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto) {
        HelpTicket ticket = helpTicketRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        if(HelpTicketUtil.canUpdateTicket(currentUser, ticket)){
            throw new UnauthorizedException("You are not authorized to update this ticket");
        }

        HelpTicketUtil.updateTicketFields(ticket, helpTicketDto);
        ticket.setUpdatedBy(currentUser.getUsername());

        ticket = helpTicketRepository.save(ticket);
        return ticketMapper.mapToTicketDto(ticket);
    }

    @LogExecution
    @Transactional
    @Override
    public HelpTicketDto assignTicket(Long id, Long assigneeId) {
        HelpTicket ticket = helpTicketRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Employee assignee = employeeRepository.findByIdAndDeletedFalse(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));

        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        if(HelpTicketUtil.canUpdateTicket(currentUser, ticket)){
            throw new UnauthorizedException("You are not authorized to update this ticket");
        }

        ticket.setAssignee(assignee);
        ticket.setUpdatedBy(currentUser.getUsername());

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
        HelpTicket ticket = helpTicketRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket id does not exists " +
                        "with given id: "+ id));

        return ticketMapper.mapToTicketDto(ticket);

    }

    // Removed?
    @Transactional
    @Override
    public void deleteTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket does not exists " +
                                "with given TicketId: "+ id));
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        ticket.setUpdatedBy(currentUser.getUsername());
        ticket.setDeleted(true);
        helpTicketRepository.save(ticket);
    }
}
