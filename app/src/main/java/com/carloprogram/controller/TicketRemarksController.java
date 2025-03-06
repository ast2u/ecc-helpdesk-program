package com.carloprogram.controller;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.service.TicketRemarksService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/remarks")
public class TicketRemarksController {

    private final TicketRemarksService ticketRemarkService;

    public TicketRemarksController(TicketRemarksService ticketRemarkService){
        this.ticketRemarkService = ticketRemarkService;
    }

    @PostMapping("/add")
    public ResponseEntity<TicketRemarksDto> createRemark(
            @Valid @RequestBody TicketRemarksDto ticketRemarksDto,
            @PathVariable("ticketId") Long ticketNumber) {
        return ResponseEntity.ok(ticketRemarkService.addRemark(ticketRemarksDto,ticketNumber));
    }

    @GetMapping
    public ResponseEntity<List<TicketRemarksDto>> getRemarksByTicket(@PathVariable("ticketId") Long ticketId) {
        return ResponseEntity.ok(ticketRemarkService.getRemarksByTicketId(ticketId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRemark(@PathVariable("ticketId") Long ticketNumber,
                                               @PathVariable("id") Long remarkId) {
        ticketRemarkService.deleteRemark(ticketNumber, remarkId);
        return ResponseEntity.ok("Remark deleted successfully");
    }
}
