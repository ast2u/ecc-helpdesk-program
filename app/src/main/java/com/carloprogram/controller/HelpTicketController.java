package com.carloprogram.controller;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class HelpTicketController {

    private final TicketService ticketService;

    public HelpTicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping("/create")
    public ResponseEntity<HelpTicketDto> createTicket(@Valid @RequestBody HelpTicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<HelpTicketDto> updateTicket(@PathVariable("id") Long id,
                                                      @RequestBody HelpTicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDto));
    }

    @PutMapping("/assign/{id}/{assigneeId}")
    public ResponseEntity<HelpTicketDto> assignTicket(@PathVariable("id") Long id,
                                                      @PathVariable("assigneeId") Long assigneeId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assigneeId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<HelpTicketDto>> getAllFilteredTickets(@ModelAttribute TicketSearchRequest request){
        Page<HelpTicketDto> ticketDtos = ticketService.getAllFilterTickets(request);
        return ResponseEntity.ok(ticketDtos);
    }

    @GetMapping("/v1/me")
    public ResponseEntity<Page<HelpTicketDto>> getUserTickets(@ModelAttribute TicketSearchRequest request){
        Page<HelpTicketDto> ticketDtos = ticketService.getUserTickets(request);
        return ResponseEntity.ok(ticketDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpTicketDto> getTicket(@PathVariable("id") Long id){
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") Long id){
        ticketService.deleteTicketById(id);
        return ResponseEntity.ok("Ticket with number:" + id + " deleted successfully.");
    }

}
