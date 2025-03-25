package com.carloprogram.service;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface TicketService {

    HelpTicketDto createTicket(HelpTicketDto helpTicketDto);
    HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto);
    HelpTicketDto assignTicket(Long id, Long assigneeId);
    Page<HelpTicketDto> getAllFilterTickets(TicketSearchRequest ticketSearch);
    Page<HelpTicketDto> getUserTickets(TicketSearchRequest ticketSearch);
    HelpTicketDto getTicketById(Long id);
    void deleteTicketById(Long id);
    long countTicketsByCreatedBy();
    long countTicketsByAssignee();
    long countUnassignedTickets();
    long countAvailableTickets();
    Map<String, Long> countTicketsByStatusCreated();
    Map<String, Long> countTicketsByStatusAssigned();
}
