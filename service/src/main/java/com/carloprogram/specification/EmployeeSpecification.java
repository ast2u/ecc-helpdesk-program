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

            //Name filter
            if(searchRequest.getName() != null && !searchRequest.getName().isEmpty()){
                String pattern = "%" + searchRequest.getName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName").get("firstName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName").get("middleName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName").get("lastName")), pattern))
                );
            }

            if(searchRequest.getBirthDate() != null){
                predicates.add(criteriaBuilder.equal(root.get("birthDate"), searchRequest.getBirthDate()));
            }

            //Address Filter
            if (searchRequest.getHouseNumber() != null) {
                predicates.add(criteriaBuilder.like(root.get("address").get("houseNumber"), "%" + searchRequest.getHouseNumber() + "%"));
            }
            if (searchRequest.getStreet() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("street")), "%" + searchRequest.getStreet().toLowerCase() + "%"));
            }
            if (searchRequest.getCity() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city")), "%" + searchRequest.getCity().toLowerCase() + "%"));
            }
            if (searchRequest.getZipCode() != null) {
                predicates.add(criteriaBuilder.like(root.get("address").get("zipCode"), "%" + searchRequest.getZipCode() + "%"));
            }


            if(searchRequest.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("employmentStatus"), searchRequest.getStatus()));
            }

            if(searchRequest.isDeleted()){
                predicates.add(criteriaBuilder.equal(root.get("deleted"), searchRequest.isDeleted()));
            }else{
                predicates.add(criteriaBuilder.isFalse(root.get("deleted")));
            }

            if(searchRequest.getCreatedBy() != null){
                String pattern = "%" + searchRequest.getCreatedBy().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("createdBy")), pattern));
            }
            if(searchRequest.getUpdatedBy() != null){
                String pattern = "%" + searchRequest.getUpdatedBy().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("updatedBy")), pattern));
            }

            if(searchRequest.getRoles() != null){
                String pattern = "%" + searchRequest.getRoles().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeRoles").get("role_title")), pattern));
            }
            if(searchRequest.getCreatedStart() != null && searchRequest.getCreatedEnd() != null){
                predicates.add(criteriaBuilder.between(root.get("createdAt"), searchRequest.getCreatedStart(), searchRequest.getCreatedEnd()));
            }
            if(searchRequest.getUpdatedStart() != null && searchRequest.getUpdatedEnd() != null){
                predicates.add(criteriaBuilder.between(root.get("updatedAt"), searchRequest.getUpdatedStart(), searchRequest.getUpdatedEnd()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
