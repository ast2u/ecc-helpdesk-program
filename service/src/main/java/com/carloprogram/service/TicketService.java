package com.carloprogram.service;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.model.HelpTicket;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {

    HelpTicketDto createTicket(HelpTicketDto helpTicketDto, Long createdById);
    HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto, Long updatedById);
    HelpTicketDto assignTicket(Long id, Long assigneeId);
    Page<HelpTicketDto> getAllTickets(int page, int size);
    Page<HelpTicketDto> searchTickets(TicketSearchRequest ticketSearch);
    HelpTicketDto getTicketById(Long id);
    void deleteTicketById(Long id);
}
