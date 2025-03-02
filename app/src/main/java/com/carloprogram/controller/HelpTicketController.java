package com.carloprogram.controller;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create/{createdById}")
    public ResponseEntity<HelpTicketDto> createTicket(@Valid @RequestBody HelpTicketDto ticketDto,
                                                      @PathVariable("createdById") Long createdById){
        return ResponseEntity.ok(ticketService.createTicket(ticketDto, createdById));
    }

    @PutMapping("/update/{id}/{updatedById}")
    public ResponseEntity<HelpTicketDto> updateTicket(@PathVariable("id") Long id,
                                                  @Valid @RequestBody HelpTicketDto ticketDto,
                                                  @PathVariable("updatedById") Long updatedById) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDto, updatedById));
    }



    @PutMapping("/assign/{id}/{assigneeId}")
    public ResponseEntity<HelpTicketDto> assignTicket(@PathVariable("id") Long id,
                                                      @PathVariable("assigneeId") Long assigneeId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assigneeId));
    }

    @GetMapping
    public ResponseEntity<Page<HelpTicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "4", name = "size") int size) {

        Page<HelpTicketDto> helpTicket = ticketService.getAllTickets(page, size);
        return ResponseEntity.ok(helpTicket);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<HelpTicketDto>> getFilteredTickets(@ModelAttribute TicketSearchRequest request){
        Page<HelpTicketDto> ticketDtos = ticketService.searchTickets(request);
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
