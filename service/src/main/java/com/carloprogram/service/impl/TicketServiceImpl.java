package com.carloprogram.service.impl;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.exception.UnauthorizedException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.HelpTicketMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.security.config.SecurityUtil;
import com.carloprogram.specification.TicketSpecification;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.TicketStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.service.TicketService;
import com.carloprogram.util.ticket.HelpTicketHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private HelpTicketHelper ticketHelper;

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
        ticket.setTicketNumber(ticketHelper.generateTicketNumber());


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

        if(ticketHelper.canUpdateTicket(currentUser, ticket)){
            throw new UnauthorizedException("You are not authorized to update this ticket");
        }

        ticketHelper.updateTicketFields(ticket, helpTicketDto);
        ticket.setUpdatedBy(currentUser.getUsername());

        ticket = helpTicketRepository.save(ticket);
        return ticketMapper.mapToTicketDto(ticket);
    }

    @LogExecution
    @Transactional
    @Override
    public HelpTicketDto assignTicket(Long id, Long assigneeId) {
        HelpTicket ticket = helpTicketRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        Employee assignee = employeeRepository.findByIdAndDeletedFalse(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        if(ticketHelper.canUpdateTicket(currentUser, ticket)){
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
    public Page<HelpTicketDto> getUserTickets(TicketSearchRequest ticketSearch){
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        Specification<HelpTicket> spec = TicketSpecification.filterTickets(ticketSearch)
                .and((root, query, cb) -> cb.or(
                        cb.equal(root.get("createdBy"), currentUser.getUsername()),
                        cb.equal(root.get("assignee"), currentUser)
                ));
        Pageable pageable = PageRequest.of(ticketSearch.getPage(), ticketSearch.getSize());

        Page<HelpTicket> tickets = helpTicketRepository.findAll(spec, pageable);

        return tickets.map(ticketMapper::mapToTicketDto);
    }

    @Transactional(readOnly = true)
    @Override
    public HelpTicketDto getTicketById(Long id) {
        HelpTicket ticket = helpTicketRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ticket id does not exists " +
                        "with given id: "+ id));

        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        if(ticketHelper.canUpdateTicket(currentUser, ticket)){
            throw new UnauthorizedException("You are not authorized to view this ticket");
        }
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

    @Override
    @Transactional(readOnly = true)
    public long countTicketsByCreatedBy() {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        return helpTicketRepository.countByCreatedByAndDeletedFalse(currentUser.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public long countTicketsByAssignee() {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        return helpTicketRepository.countByAssignee_IdAndDeletedFalse(currentUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnassignedTickets() {
        return helpTicketRepository.countByAssignee_IdAndDeletedFalse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAvailableTickets() {
        return helpTicketRepository.countByDeletedFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> countTicketsByStatusCreated() {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        List<Object[]> results = helpTicketRepository.countTicketsByStatusCreated(currentUser.getUsername());
        return results.stream()
                .collect(Collectors.toMap(
                        result -> ((TicketStatus) result[0]).name(),
                        result -> (Long) result[1]
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> countTicketsByStatusAssigned() {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();
        List<Object[]> results = helpTicketRepository.countTicketsByStatusAssigned(currentUser.getId());
        return results.stream()
                .collect(Collectors.toMap(
                        result -> ((TicketStatus) result[0]).name(),
                        result -> (Long) result[1]
                ));
    }
}
