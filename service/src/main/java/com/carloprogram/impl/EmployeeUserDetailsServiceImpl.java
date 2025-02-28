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

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByUsername(username);
        if(employee == null){
            System.out.println("Employee not found");
            throw new UsernameNotFoundException("Employee not found");
        }

        return new EmployeeUserPrincipal(employee);
    }
}
