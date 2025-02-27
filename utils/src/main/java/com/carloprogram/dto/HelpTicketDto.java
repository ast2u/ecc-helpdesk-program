package com.carloprogram.dto;

import com.carloprogram.model.TicketRemarks;
import com.carloprogram.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpTicketDto {
    private Long id;
    private String ticketNumber;
    private String title;
    private String body;
    private TicketStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private EmployeeDto assigneeId;
    private EmployeeDto createdById;
    private EmployeeDto updatedById;
    private List<TicketRemarksDto> remarks;
}
