package com.carloprogram.controller;

import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<HelpTicketDto> updateTicket(@PathVariable("id") Long id,
                                                  @RequestBody HelpTicketDto ticketDto,
                                                  @PathVariable("updatedById") Long updatedById) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDto, updatedById));
    }



    @PutMapping("/assign/{ticketNumber}/{assigneeId}")
    public ResponseEntity<HelpTicketDto> assignTicket(@PathVariable("id") Long id,
                                                      @PathVariable("assigneeId") Long assigneeId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assigneeId));
    }

    @GetMapping
    public ResponseEntity<Page<HelpTicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "3", name = "size") int size) {

        Page<HelpTicketDto> helpTicket = ticketService.getAllTickets(page, size);
        return ResponseEntity.ok(helpTicket);
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
