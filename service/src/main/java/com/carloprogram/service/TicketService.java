package com.carloprogram.service;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import org.springframework.data.domain.Page;

public interface TicketService {

    HelpTicketDto createTicket(HelpTicketDto helpTicketDto);
    HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto);
    HelpTicketDto assignTicket(Long id, Long assigneeId);
    Page<HelpTicketDto> getAllFilterTickets(TicketSearchRequest ticketSearch);
    HelpTicketDto getTicketById(Long id);
    void deleteTicketById(Long id);
}
