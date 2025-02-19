package com.carloprogram.impl;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.mapper.TicketRemarksMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.TicketRemarks;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.repository.HelpTicketRepository;
import com.carloprogram.repository.TicketRemarksRepository;
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

    @Transactional
    @Override
    public TicketRemarksDto addRemark(TicketRemarksDto ticketRemarksDto, Long ticketNumber, Long employeeId) {
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticketNumber: " + ticketNumber));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        TicketRemarks remark = TicketRemarksMapper.mapToTicketRemarks(ticketRemarksDto, ticket, employee);

        TicketRemarks savedRemark = ticketRemarksRepository.save(remark);

        return TicketRemarksMapper.mapToTicketRemarksDto(savedRemark);
    }

    @Override
    public List<TicketRemarksDto> getRemarksByTicketId(Long ticketId) {

        HelpTicket ticket = helpTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticketNumber: " + ticketId));

        List<TicketRemarks> remarks = ticketRemarksRepository.findByTicketNumber(ticket);

        return remarks.stream()
                .map(TicketRemarksMapper::mapToTicketRemarksDto)
                .collect(Collectors.toList());
    }

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
