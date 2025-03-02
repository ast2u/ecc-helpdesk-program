package com.carloprogram.mapper;

import com.carloprogram.dto.HelpTicketDto;
import com.carloprogram.model.HelpTicket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, TicketRemarksMapper.class})
public interface HelpTicketMapper {

    HelpTicketMapper INSTANCE = Mappers.getMapper(HelpTicketMapper.class);

    HelpTicketDto mapToTicketDto(HelpTicket ticket);

    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    HelpTicket mapToTicket(HelpTicketDto dto);

}