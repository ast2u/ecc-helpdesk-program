package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.TicketRemarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRemarksRepository extends JpaRepository<TicketRemarks, Long> {
    List<TicketRemarks> findByTicketNumber(HelpTicket ticket);
}
