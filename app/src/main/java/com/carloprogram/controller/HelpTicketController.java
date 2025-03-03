package com.carloprogram.controller;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class HelpTicketController {
    /*
    TODO: Admin must be able to add a remark and update status of the ticket.
     Must be able to assign ticket to an employee
    */

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<HelpTicketDto> createTicket(@Valid @RequestBody HelpTicketDto ticketDto,
                                                      @AuthenticationPrincipal EmployeeUserPrincipal userPrincipal) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDto, userPrincipal));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<HelpTicketDto> updateTicket(@PathVariable("id") Long id,
                                                      @RequestBody HelpTicketDto ticketDto,
                                                      @AuthenticationPrincipal EmployeeUserPrincipal userPrincipal) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDto, userPrincipal));
    }

    @PutMapping("/assign/{id}/{assigneeId}")
    public ResponseEntity<HelpTicketDto> assignTicket(@PathVariable("id") Long id,
                                                      @PathVariable("assigneeId") Long assigneeId,
                                                      @AuthenticationPrincipal EmployeeUserPrincipal userPrincipal) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assigneeId, userPrincipal));
    }

    @GetMapping
    public ResponseEntity<Page<HelpTicketDto>> getAllFilteredTickets(@ModelAttribute TicketSearchRequest request){
        Page<HelpTicketDto> ticketDtos = ticketService.getAllFilterTickets(request);
        return ResponseEntity.ok(ticketDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpTicketDto> getTicket(@PathVariable("id") Long id){
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") Long id){
        try {
            ticketService.deleteTicketById(id);
            return ResponseEntity.ok("Ticket with number:" + id + " deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
        }
    }

}
