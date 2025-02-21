package com.carloprogram.service;

import com.carloprogram.dto.HelpTicketDto;

import java.util.List;

public interface TicketService {

    HelpTicketDto createTicket(HelpTicketDto helpTicketDto, Long createdById);
    HelpTicketDto updateTicket(Long ticketNumber, HelpTicketDto helpTicketDto, Long updatedById);
    HelpTicketDto assignTicket(Long ticketNumber, Long assigneeId);
    List<HelpTicketDto> getAllTickets();
    HelpTicketDto getTicketById(Long ticketNumber);
    void deleteTicketById(Long ticketNumber);
}
