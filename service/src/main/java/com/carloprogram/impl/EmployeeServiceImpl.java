package com.carloprogram.impl;

import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.dto.LoginRequest;
import com.carloprogram.dto.LoginResponse;
import com.carloprogram.dto.search.EmployeeSearchRequest;
import com.carloprogram.exception.ResourceNotFoundException;
import com.carloprogram.logging.LogExecution;
import com.carloprogram.mapper.EmployeeMapper;
import com.carloprogram.mapper.EmployeeProfileMapper;
import com.carloprogram.model.Employee;
import com.carloprogram.model.EmployeeRole;
import com.carloprogram.model.EmployeeUserPrincipal;
import com.carloprogram.model.enums.EmploymentStatus;
import com.carloprogram.repository.EmployeeRepository;
import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.repository.EmployeeRoleRepository;
import com.carloprogram.security.service.JwtService;
import com.carloprogram.service.EmployeeService;
import com.carloprogram.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeProfileMapper profileMapper;
    /**
     TODO: Implement method for changing password for employee user
     */

    @Override
    @LogExecution
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                            , loginRequest.getPassword()));

            EmployeeUserPrincipal userPrincipal = (EmployeeUserPrincipal) authentication.getPrincipal();

            Employee employee = employeeRepository.findByUsernameAndDeletedFalse(userPrincipal.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

            EmployeeDto employeeDto = employeeMapper.mapToEmployeeDto(employee);

            String token = jwtService.generateToken(userPrincipal);

            return ResponseEntity.ok(new LoginResponse(token,employeeDto));

        }catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @Transactional
    @LogExecution
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto, EmployeeUserPrincipal userPrincipal) {
        Employee currentUser = employeeRepository.findById(userPrincipal.getEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employee employee = employeeMapper.mapToEmployee(employeeDto);
        employee.setPassword(encoder.encode("Passw0rd123"));

        if (employeeDto.getEmployeeRoles() != null) {
            List<EmployeeRole> roles = employeeDto.getEmployeeRoles()
                    .stream()
                    .map(roleId -> employeeRoleRepository.findById(roleId.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            employee.setEmployeeRoles(roles);
        } else {
            employee.setEmployeeRoles(null);
        }

        if (employee.getEmploymentStatus() == null) {
            employee.setEmploymentStatus(EmploymentStatus.FULL_TIME);
        }

        employee.setCreatedBy(currentUser);
        employee.setUpdatedBy(currentUser);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeProfileDto getEmployeeProfile(EmployeeUserPrincipal userPrincipal) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(userPrincipal
                        .getEmployee()
                        .getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist"));

        return profileMapper.toProfileDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeDto> getAllEmployees(EmployeeSearchRequest searchRequest) {
        Specification<Employee> spec = EmployeeSpecification.filterEmployees(searchRequest);

        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(),
                Sort.by("id").ascending());

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        return employeePage.map(employeeMapper::mapToEmployeeDto);
    }

    @Transactional
    @LogExecution
    @Override
    public EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee, EmployeeUserPrincipal userPrincipal) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        Employee currentUser = userPrincipal.getEmployee(); // Get the logged-in user directly

        // Only update fields if provided
        Optional.ofNullable(updatedEmployee.getFirstName()).ifPresent(employee::setFirstName);
        Optional.ofNullable(updatedEmployee.getLastName()).ifPresent(employee::setLastName);
        Optional.ofNullable(updatedEmployee.getBirthDate()).ifPresent(employee::setBirthDate);
        Optional.ofNullable(updatedEmployee.getAddress()).ifPresent(employee::setAddress);
        Optional.ofNullable(updatedEmployee.getContactNumber()).ifPresent(employee::setContactNumber);
        Optional.ofNullable(updatedEmployee.getEmploymentStatus()).ifPresent(employee::setEmploymentStatus);

        if (updatedEmployee.getEmployeeRoles() != null) {
            List<EmployeeRole> roles = updatedEmployee.getEmployeeRoles().stream()
                    .map(roleDto -> employeeRoleRepository.findById(roleDto.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            employee.setEmployeeRoles(roles);
        }

        employee.setUpdatedBy(currentUser);

        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Transactional
    @LogExecution
    @Override
    public void deleteEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists " +
                                "with given id: "+ employeeId));
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    @LogExecution
    public EmployeeDto assignRoleToEmployee(Long employeeId, List<Long> roleIds, EmployeeUserPrincipal userPrincipal) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Employee currentUser = employeeRepository.findById(userPrincipal.getEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<EmployeeRole> roles = roleIds.stream()
                .map(roleId -> employeeRoleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId)))
                .collect(Collectors.toList());

        employee.setEmployeeRoles(roles);

        employee.setUpdatedBy(currentUser);

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.mapToEmployeeDto(updatedEmployee);
    }

}
