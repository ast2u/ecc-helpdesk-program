package com.carloprogram.impl;

import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public EmployeeUserDetailsServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employee Not Found with username: " + username));

        return new EmployeeUserPrincipal(employee);
    }
}
