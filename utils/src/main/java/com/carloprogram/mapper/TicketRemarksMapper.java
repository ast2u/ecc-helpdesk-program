package com.carloprogram.mapper;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.model.TicketRemarks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TicketRemarksMapper {

    TicketRemarksMapper INSTANCE = Mappers.getMapper(TicketRemarksMapper.class);

    @Mapping(source = "ticketId.id", target = "ticketId")
    @Mapping(source = "employeeId.id", target = "employeeId")
    TicketRemarksDto mapToTicketRemarksDto(TicketRemarks ticketRemarks);

    @Mapping(target = "ticketId", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    TicketRemarks mapToTicketRemarks(TicketRemarksDto dto);
}
