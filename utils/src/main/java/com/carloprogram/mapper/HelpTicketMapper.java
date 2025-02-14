package com.carloprogram.mapper;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;

public class HelpTicketMapper {
    public static HelpTicketDto mapToTicketDto(HelpTicket ticket) {
        return new HelpTicketDto(
                ticket.getTicketNumber(),
                ticket.getTicketTitle(),
                ticket.getBody(),
                ticket.getStatus(),
                ticket.getCreatedDate(),
                ticket.getUpdatedDate(),
                ticket.getAssignee() != null ? ticket.getAssignee().getId() : null,
                ticket.getCreatedBy() != null ? ticket.getCreatedBy().getId() : null,
                ticket.getUpdatedBy() != null ? ticket.getUpdatedBy().getId() : null,
                ticket.getRemarks()
        );
    }

    public static HelpTicket mapToTicket(HelpTicketDto dto, Employee assignee, Employee createdBy, Employee updatedBy) {
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
                dto.getRemarks()
        );
    }
}
