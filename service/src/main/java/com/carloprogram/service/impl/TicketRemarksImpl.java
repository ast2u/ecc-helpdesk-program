package com.carloprogram.service.impl;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.TicketRemarksMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.TicketRemarks;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.repository.TicketRemarksRepository;
import com.carloprogram.security.config.SecurityUtil;
import com.carloprogram.service.TicketRemarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketRemarksImpl implements TicketRemarksService {

    @Autowired
    private TicketRemarksRepository ticketRemarksRepository;

    @Autowired
    private HelpTicketRepository helpTicketRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TicketRemarksMapper ticketRemarksMapper;

    @Autowired
    private SecurityUtil securityUtil;

    @Transactional
    @LogExecution
    @Override
    public TicketRemarksDto addRemark(TicketRemarksDto ticketRemarksDto, Long ticketNumber) {
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticketNumber: " + ticketNumber));

        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        TicketRemarks remark = ticketRemarksMapper.mapToTicketRemarks(ticketRemarksDto);
        remark.setTicket(ticket);
        remark.setEmployeeId(currentUser);

        TicketRemarks savedRemark = ticketRemarksRepository.save(remark);

        return ticketRemarksMapper.mapToTicketRemarksDto(savedRemark);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketRemarksDto> getRemarksByTicketId(Long ticketId) {

        HelpTicket ticket = helpTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticketNumber: " + ticketId));

        List<TicketRemarks> remarks = ticketRemarksRepository.findByTicket(ticket);

        return remarks.stream()
                .map(ticketRemarksMapper::mapToTicketRemarksDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @LogExecution
    @Override
    public void deleteRemark(Long ticketId, Long id) {
        HelpTicket ticket = helpTicketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket not found with ticketNumber: "+ ticketId));

        TicketRemarks ticketRemarks = ticketRemarksRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("This remark does not exist"));

        ticketRemarksRepository.delete(ticketRemarks);
    }


}
