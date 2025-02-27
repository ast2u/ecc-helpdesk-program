package com.carloprogram.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Comment is mandatory")
    private String comment;

    private LocalDateTime createdDate;
}
