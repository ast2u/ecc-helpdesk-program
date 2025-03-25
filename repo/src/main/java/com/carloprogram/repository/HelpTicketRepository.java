package com.carloprogram.repository;

import com.carloprogram.model.HelpTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HelpTicketRepository extends JpaRepository<HelpTicket, Long>, JpaSpecificationExecutor<HelpTicket> {
    Optional<HelpTicket> findByIdAndDeletedFalse(Long id);

    @Query("SELECT h.ticketNumber FROM HelpTicket h ORDER BY h.id DESC LIMIT 1")
    String findLastTicketNumber();

    long countByCreatedBy(String username);
    long countByAssignee_Id(Long employeeId);

    @Query("SELECT h.status, COUNT(h) FROM HelpTicket h WHERE h.createdBy = :username GROUP BY h.status")
    List<Object[]> countTicketsByStatusCreated(@Param("username") String username);

    @Query("SELECT h.status, COUNT(h) FROM HelpTicket h WHERE h.assignee.id = :employeeId GROUP BY h.status")
    List<Object[]> countTicketsByStatusAssigned(@Param("employeeId") Long employeeId);
}
