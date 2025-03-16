package com.carloprogram.specification;

import com.carloprogram.dto.search.TicketSearchRequest;
import com.carloprogram.model.HelpTicket;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TicketSpecification {

    public static Specification<HelpTicket> filterTickets(TicketSearchRequest searchRequest){
        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(searchRequest.getTicketNumber() != null){
                predicates.add(criteriaBuilder.like(root.get("ticketNumber"),"%" + searchRequest.getTicketNumber() + "%"));
            }

            if(searchRequest.getDesc() != null && !searchRequest.getDesc().isEmpty()){
                String pattern = "%" + searchRequest.getDesc().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or
                        (criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        (criteriaBuilder.like(criteriaBuilder.lower(root.get("body")), pattern))
                        ));
            }

            if(searchRequest.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), searchRequest.getStatus()));
            }

            if(searchRequest.getCreatedBy() != null){
                String pattern = "%" + searchRequest.getCreatedBy().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("createdBy")), pattern));
            }
            if(searchRequest.getUpdatedBy() != null){
                String pattern = "%" + searchRequest.getUpdatedBy().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("updatedBy")), pattern));
            }

            if(searchRequest.getAssignee() != null){
                predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), searchRequest.getAssignee()));
            }
            if(searchRequest.getCreatedStart() != null && searchRequest.getCreatedEnd() != null){
                predicates.add(criteriaBuilder.between(root.get("createdDate"), searchRequest.getCreatedStart(), searchRequest.getCreatedEnd()));
            }
            if(searchRequest.getUpdatedStart() != null && searchRequest.getUpdatedEnd() != null){
                predicates.add(criteriaBuilder.between(root.get("updatedDate"), searchRequest.getUpdatedStart(), searchRequest.getUpdatedEnd()));
            }

            if(searchRequest.isDeleted()){
                predicates.add(criteriaBuilder.equal(root.get("deleted"), searchRequest.isDeleted()));
            }else{
                predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
