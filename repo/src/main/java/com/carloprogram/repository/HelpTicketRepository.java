package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long>, JpaSpecificationExecutor<HelpTicket> {
    Optional<HelpTicket> findByIdAndDeletedFalse(Long id);
}
