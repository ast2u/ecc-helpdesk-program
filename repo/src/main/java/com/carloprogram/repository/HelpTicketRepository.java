package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long>, JpaSpecificationExecutor<HelpTicket> {
    Optional<HelpTicket> findByIdAndDeletedFalse(Long id);

    @Query("SELECT h.ticketNumber FROM HelpTicket h ORDER BY h.id DESC LIMIT 1")
    String findLastTicketNumber();
}
