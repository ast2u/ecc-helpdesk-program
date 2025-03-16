package com.carloprogram.mapper;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.model.HelpTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, TicketRemarksMapper.class})
public interface HelpTicketMapper {
    HelpTicketDto mapToTicketDto(HelpTicket ticket);

    @Mapping(target = "ticketNumber", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    HelpTicket mapToTicket(HelpTicketDto dto);

}