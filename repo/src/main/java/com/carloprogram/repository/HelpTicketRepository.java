package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long> {
    Page<HelpTicket> findAll(Pageable pageable);
}
