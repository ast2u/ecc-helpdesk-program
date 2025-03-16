package com.carloprogram.dto.search;

import com.carloprogram.model.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketSearchRequest extends BaseSearchRequest {
    private String ticketNumber;
    private String desc;
    private TicketStatus status;
    private Long assignee;



}
