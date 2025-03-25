package com.carloprogram.controller;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteTicket(@PathVariable("id") Long id){
        ticketService.deleteTicketById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee marked as deleted");
        response.put("employeeId", id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("v1/count/available")
    public ResponseEntity<Long> getAvailableTicketCount() {
        return ResponseEntity.ok(ticketService.countAvailableTickets());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("v1/count/unassigned")
    public ResponseEntity<Long> getUnassignedTicketCount() {
        return ResponseEntity.ok(ticketService.countUnassignedTickets());
    }

    @GetMapping("v1/me/count/created")
    public ResponseEntity<Long> getCreatedTicketCount() {
        long count = ticketService.countTicketsByCreatedBy();
        return ResponseEntity.ok(count);
    }

    @GetMapping("v1/me/count/assigned")
    public ResponseEntity<Long> getAssignedTicketCount() {
        long count = ticketService.countTicketsByAssignee();
        return ResponseEntity.ok(count);
    }

    @GetMapping("v1/me/count/status/created")
    public ResponseEntity<Map<String, Long>> getCreatedTicketStatusCount() {
        return ResponseEntity.ok(ticketService.countTicketsByStatusCreated());
    }

    @GetMapping("v1/me/count/status/assigned")
    public ResponseEntity<Map<String, Long>> getAssignedTicketStatusCount() {
        return ResponseEntity.ok(ticketService.countTicketsByStatusAssigned());
    }

}
