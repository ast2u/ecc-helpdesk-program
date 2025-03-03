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

            if(searchRequest.getDesc() != null && !searchRequest.getDesc().isEmpty()){
                String pattern = "%" + searchRequest.getDesc().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or
                        (criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        (criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern))
                        ));
            }
            if(searchRequest.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), searchRequest.getStatus()));
            }
            if(searchRequest.getCreatedBy() != null){
                predicates.add(criteriaBuilder.equal(root.get("createdBy").get("id"), searchRequest.getCreatedBy()));
            }
            if(searchRequest.getUpdatedBy() != null){
                predicates.add(criteriaBuilder.equal(root.get("updatedBy").get("id"), searchRequest.getUpdatedBy()));
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
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
