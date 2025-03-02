package com.carloprogram.service;

import com.carloprogram.dto.TicketRemarksDto;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.model.TicketRemarks;

import java.util.List;

public interface TicketRemarksService {
    TicketRemarksDto addRemark(TicketRemarksDto ticketRemarksDto, Long ticketNumber, EmployeeUserPrincipal employeeUserPrincipal);
    List<TicketRemarksDto> getRemarksByTicketId(Long ticketId);
    void deleteRemark(Long ticketId, Long id);
}
