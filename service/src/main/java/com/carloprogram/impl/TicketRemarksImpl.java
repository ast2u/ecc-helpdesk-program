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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketRemarksImpl implements TicketRemarksService {

    TicketRemarksRepository ticketRemarksRepository;
    HelpTicketRepository helpTicketRepository;
    EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public TicketRemarksDto addRemark(TicketRemarksDto ticketRemarksDto, Long ticketNumber, Long employeeId) {
        // Fetch the ticket by ticketNumber
        HelpTicket ticket = helpTicketRepository.findById(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ticketNumber: " + ticketNumber));

        // Fetch the employee who wrote the remark
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        // Create a new TicketRemarks object
        TicketRemarks remark = TicketRemarksMapper.mapToTicketRemarks(ticketRemarksDto, ticket, employee);

        // Save the remark to the database
        TicketRemarks savedRemark = ticketRemarksRepository.save(remark);

        // Return the mapped DTO
        return TicketRemarksMapper.mapToTicketRemarksDto(savedRemark);
    }

    @Override
    public List<TicketRemarksDto> getRemarksByTicketId(Long ticketId) {
        return ticketRemarksRepository.findById(ticketId)
                .stream()
                .map(TicketRemarksMapper::mapToTicketRemarksDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRemark(Long ticketId, Long id) {
        HelpTicket ticket = helpTicketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ticket does not exists" +
                                "with given Ticket Number: "+ ticketId));

        TicketRemarks ticketRemarks = ticketRemarksRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("This Remark does not exists"));

        if (!ticketRemarks.getTicketNumber().equals(ticket)) {
            throw new IllegalArgumentException("Remark does not belong to the specified ticket");
        }

        ticketRemarksRepository.delete(ticketRemarks);
    }


}
