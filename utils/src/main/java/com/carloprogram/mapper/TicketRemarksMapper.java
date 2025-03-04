package com.carloprogram.mapper;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.model.TicketRemarks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketRemarksMapper {

    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "employeeId.id", target = "employeeId")
    TicketRemarksDto mapToTicketRemarksDto(TicketRemarks ticketRemarks);

    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    TicketRemarks mapToTicketRemarks(TicketRemarksDto dto);
}
