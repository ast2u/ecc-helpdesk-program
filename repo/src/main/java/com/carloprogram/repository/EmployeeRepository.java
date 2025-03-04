package com.carloprogram.repository;

import com.carloprogram.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @EntityGraph(attributePaths = "employeeRoles")
    Optional<Employee> findByUsernameAndDeletedFalse(String username);

    Optional<Employee> findByIdAndDeletedFalse(Long id);
}
