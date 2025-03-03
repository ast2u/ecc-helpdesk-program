package com.carloprogram.util.ticket;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;

import java.util.Optional;

public class HelpTicketUtil {

    private HelpTicketUtil(){}

    public static boolean canUpdateTicket(Employee user, HelpTicket ticket) {
        boolean isAdmin = user.getEmployeeRoles().stream()
                .anyMatch(role -> role.getRole_title().equals("ADMIN"));

        boolean isCreator = ticket.getCreatedBy().getId().equals(user.getId());
        boolean isAssignee = ticket.getAssignee() != null && ticket.getAssignee().getId().equals(user.getId());

        return !isAdmin && !isCreator && !isAssignee;
    }

    public static void updateTicketFields(HelpTicket ticket, HelpTicketDto helpTicketDto) {
        Optional.ofNullable(helpTicketDto.getTitle()).ifPresent(ticket::setTitle);
        Optional.ofNullable(helpTicketDto.getBody()).ifPresent(ticket::setBody);
        Optional.ofNullable(helpTicketDto.getStatus()).ifPresent(ticket::setStatus);
    }
}
