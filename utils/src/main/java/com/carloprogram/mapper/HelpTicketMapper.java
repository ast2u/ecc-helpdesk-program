package com.carloprogram.mapper;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.model.HelpTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, TicketRemarksMapper.class})
public interface HelpTicketMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "updatedBy", source = "updatedBy.id")
    HelpTicketDto mapToTicketDto(HelpTicket ticket);

    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    HelpTicket mapToTicket(HelpTicketDto dto);

}