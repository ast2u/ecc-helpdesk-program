package com.carloprogram.repository;
import com.carloprogram.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = "employeeRoles")
    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByIdAndDeletedFalse(Long id);

    Page<Employee> findAll(Pageable pageable);

    List<Employee> findAll(); // For non-paginated lists
}
