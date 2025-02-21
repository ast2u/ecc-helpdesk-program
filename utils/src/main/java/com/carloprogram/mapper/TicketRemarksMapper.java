package com.carloprogram.mapper;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.TicketRemarks;

public class TicketRemarksMapper {
    public static TicketRemarksDto mapToTicketRemarksDto(TicketRemarks ticketRemarks) {
        return new TicketRemarksDto(
                ticketRemarks.getId(),
                ticketRemarks.getTicketNumber() != null ? ticketRemarks.getTicketNumber().getTicketNumber() : null,
                ticketRemarks.getEmployeeId() != null ? ticketRemarks.getEmployeeId().getId() : null,
                ticketRemarks.getComment(),
                ticketRemarks.getCreatedDate()
        );
    }

    public static TicketRemarks mapToTicketRemarks(TicketRemarksDto dto, HelpTicket ticketNumber, Employee employeeId) {
        return new TicketRemarks(
                dto.getId(),
                ticketNumber,
                employeeId,
                dto.getComment(),
                dto.getCreatedDate()
        );
    }
}
