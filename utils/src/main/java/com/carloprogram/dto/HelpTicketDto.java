package com.carloprogram.dto;

import com.carloprogram.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpTicketDto {
    private Long ticketNumber;
    private String title;
    private String body;
    private TicketStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long assigneeId;
    private Long createdById;
    private Long updatedById;
    private String remarks;
}
