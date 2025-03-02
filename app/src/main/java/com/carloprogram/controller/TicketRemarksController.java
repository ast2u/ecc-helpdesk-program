package com.carloprogram.controller;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.service.TicketRemarksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/remarks")
public class TicketRemarksController {

    @Autowired
    private TicketRemarksService ticketRemarkService;

    @PostMapping("/add")
    public ResponseEntity<TicketRemarksDto> createRemark(
            @Valid @RequestBody TicketRemarksDto ticketRemarksDto,
            @PathVariable("ticketId") Long ticketNumber,
            @AuthenticationPrincipal EmployeeUserPrincipal userPrincipal) {
        return ResponseEntity.ok(ticketRemarkService.addRemark(ticketRemarksDto,ticketNumber, userPrincipal));
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
