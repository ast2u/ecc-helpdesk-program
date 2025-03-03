package com.carloprogram.specification;

import com.carloprogram.dto.search.EmployeeSearchRequest;
import com.carloprogram.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filterEmployees(EmployeeSearchRequest searchRequest){
        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(searchRequest.getName() != null && !searchRequest.getName().isEmpty()){
                String pattern = "%" + searchRequest.getName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern))
                );
            }

            if(searchRequest.getBirthDate() != null){
                predicates.add(criteriaBuilder.equal(root.get("birthDate"), searchRequest.getBirthDate()));
            }
            if(searchRequest.getAddress() != null){
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + searchRequest.getAddress() + "%"));
            }
            if(searchRequest.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("employmentStatus"), searchRequest.getStatus()));
            }

            if(searchRequest.isDeleted()){
                predicates.add(criteriaBuilder.equal(root.get("deleted"), searchRequest.isDeleted()));
            }else{
                predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
            }

            if(searchRequest.getRoles() != null){
                predicates.add(root.join("employeeRoles").get("id").in(searchRequest.getRoles()));
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
