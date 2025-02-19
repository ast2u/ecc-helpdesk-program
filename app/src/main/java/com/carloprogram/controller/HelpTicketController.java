package com.carloprogram.controller;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<HelpTicketDto> createTicket(@RequestBody HelpTicketDto ticketDto,
                                                      @PathVariable("createdById") Long createdById){
        return ResponseEntity.ok(ticketService.createTicket(ticketDto, createdById));
    }

    @PutMapping("/update/{ticketNumber}/{updatedById}")
    public ResponseEntity<HelpTicketDto> updateTicket(@PathVariable("ticketNumber") Long ticketNumber,
                                                  @RequestBody HelpTicketDto ticketDto,
                                                  @PathVariable("updatedById") Long updatedById) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketNumber, ticketDto, updatedById));
    }



    @PutMapping("/assign/{ticketNumber}/{assigneeId}")
    public ResponseEntity<HelpTicketDto> assignTicket(@PathVariable("ticketNumber") Long ticketNumber,
                                                      @PathVariable("assigneeId") Long assigneeId) {
        return ResponseEntity.ok(ticketService.assignTicket(ticketNumber, assigneeId));
    }

    @GetMapping
    public ResponseEntity<List<HelpTicketDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HelpTicketDto> getTicket(@PathVariable("id") Long ticketNumber){
        return ResponseEntity.ok(ticketService.getTicketById(ticketNumber));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long ticketNumber){
        try {
            ticketService.deleteTicketById(ticketNumber);
            return ResponseEntity.ok("Ticket with number:" + ticketNumber + " deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
        }
    }

}
