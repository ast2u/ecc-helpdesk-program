package com.carloprogram.security.config;

import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.exception.UnauthorizedException;
import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.repository.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private final EmployeeRepository employeeRepository;

    public SecurityUtil(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeUserPrincipal getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("User is not authenticated");
        }

        return (EmployeeUserPrincipal) authentication.getPrincipal();
    }

    public Employee getAuthenticatedEmployee() {
        return employeeRepository.findByUsernameAndDeletedFalse(getAuthenticatedUser().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist"));
    }
}
