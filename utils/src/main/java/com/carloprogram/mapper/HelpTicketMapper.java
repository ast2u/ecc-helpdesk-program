package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.TicketRemarks;

import java.util.List;
import java.util.stream.Collectors;

public class HelpTicketMapper {
    public static HelpTicketDto mapToTicketDto(HelpTicket ticket) {
        List<TicketRemarksDto> remarkDtos = (ticket.getRemarks() != null) ?
                ticket.getRemarks()
                        .stream()
                        .map(TicketRemarksMapper::mapToTicketRemarksDto)
                        .collect(Collectors.toList())
                : null;


        return new HelpTicketDto(
                ticket.getTicketNumber(),
                ticket.getTicketTitle(),
                ticket.getBody(),
                ticket.getStatus(),
                ticket.getCreatedDate(),
                ticket.getUpdatedDate(),
                ticket.getAssignee() != null ? EmployeeMapper.mapToEmployeeDto(ticket.getAssignee()) : null,
                EmployeeMapper.mapToEmployeeDto(ticket.getCreatedBy()),
                ticket.getUpdatedBy() != null ? EmployeeMapper.mapToEmployeeDto(ticket.getUpdatedBy()) : null,
                remarkDtos
        );
    }

    public static HelpTicket mapToTicket(HelpTicketDto dto, Employee assignee, Employee createdBy, Employee updatedBy, List<TicketRemarks> ticketRemarks) {
        return new HelpTicket(
                dto.getTicketNumber(),
                dto.getTitle(),
                dto.getBody(),
                dto.getStatus(),
                dto.getCreatedDate(),
                dto.getUpdatedDate(),
                assignee,
                createdBy,
                updatedBy,
                ticketRemarks
        );
    }
}
