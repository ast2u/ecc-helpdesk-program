package com.carloprogram.specification;

import com.carloprogram.model.HelpTicket;
import com.carloprogram.model.enums.TicketStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TicketSpecification {

    public static Specification<HelpTicket> hasStatus(TicketStatus status) {
        return (root, query, criteriaBuilder) ->
                status != null ? criteriaBuilder.equal(root.get("status"), status) : null;
    }

    public static Specification<HelpTicket> createdBy(Long createdBy) {
        return (root, query, criteriaBuilder) ->
                createdBy != null ? criteriaBuilder.equal(root.get("createdBy").get("id"), createdBy) : null;
    }

    public static Specification<HelpTicket> updatedBy(Long updatedBy) {
        return (root, query, criteriaBuilder) ->
                updatedBy != null ? criteriaBuilder.equal(root.get("updatedBy").get("id"), updatedBy) : null;
    }

    public static Specification<HelpTicket> assignedTo(Long assignee) {
        return (root, query, criteriaBuilder) ->
                assignee != null ? criteriaBuilder.equal(root.get("assignee").get("id"), assignee) : null;
    }

    public static Specification<HelpTicket> createdBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("createdDate"), start, end);
            }
            return null;
        };
    }

    public static Specification<HelpTicket> updatedBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("updatedDate"), start, end);
            }
            return null;
        };
    }
}
