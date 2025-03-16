package com.carloprogram.util.ticket;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.model.Employee;
import com.carloprogram.model.HelpTicket;
import com.carloprogram.repository.HelpTicketRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HelpTicketHelper {

    private final HelpTicketRepository ticketRepository;

    public HelpTicketHelper(HelpTicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    public boolean canUpdateTicket(Employee user, HelpTicket ticket) {
        boolean isAdmin = user.getEmployeeRoles().stream()
                .anyMatch(role -> role.getRole_title().equals("ADMIN"));

        boolean isCreator = ticket.getCreatedBy().equals(user.getUsername());
        boolean isAssignee = ticket.getAssignee() != null && ticket.getAssignee().getId().equals(user.getId());

        return !isAdmin && !isCreator && !isAssignee;
    }

    public void updateTicketFields(HelpTicket ticket, HelpTicketDto helpTicketDto) {
        Optional.ofNullable(helpTicketDto.getTitle()).ifPresent(ticket::setTitle);
        Optional.ofNullable(helpTicketDto.getBody()).ifPresent(ticket::setBody);
        Optional.ofNullable(helpTicketDto.getStatus()).ifPresent(ticket::setStatus);
    }

    public String generateTicketNumber() {
        String lastTicketNumber = ticketRepository.findLastTicketNumber();
        int nextNumber = (lastTicketNumber != null) ? Integer.parseInt(lastTicketNumber) + 1 : 1;
        return String.format("%04d", nextNumber);
    }
}
