package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long>, JpaSpecificationExecutor<HelpTicket> {
}
