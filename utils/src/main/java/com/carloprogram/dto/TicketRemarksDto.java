package com.carloprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRemarksDto {
    private Long id;
    private Long ticketId;
    private Long employeeId;
    private String comment;
    private LocalDateTime createdDate;
}
