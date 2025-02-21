package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long> {
}
