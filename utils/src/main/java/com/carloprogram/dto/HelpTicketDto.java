package com.carloprogram.dto;

import com.carloprogram.model.TicketRemarks;
import com.carloprogram.model.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
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
public class HelpTicketDto extends BaseDto {
    private String ticketNumber;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Body is mandatory")
    private String body;
    private TicketStatus status;
    private EmployeeProfileDto assignee;
    private List<TicketRemarksDto> remarks;
}
