package com.carloprogram.service;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.model.HelpTicket;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {

    HelpTicketDto createTicket(HelpTicketDto helpTicketDto, EmployeeUserPrincipal userPrincipal);
    HelpTicketDto updateTicket(Long id, HelpTicketDto helpTicketDto, EmployeeUserPrincipal userPrincipal);
    HelpTicketDto assignTicket(Long id, Long assigneeId, EmployeeUserPrincipal userPrincipal);
    Page<HelpTicketDto> getAllFilterTickets(TicketSearchRequest ticketSearch);
    HelpTicketDto getTicketById(Long id);
    void deleteTicketById(Long id);
}
