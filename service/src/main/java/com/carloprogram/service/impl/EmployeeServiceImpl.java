package com.carloprogram.service.impl;

import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.dto.login.LoginRequest;
import com.carloprogram.dto.login.LoginResponse;
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
import com.carloprogram.security.config.SecurityUtil;
import com.carloprogram.security.service.JwtService;
import com.carloprogram.service.EmployeeService;
import com.carloprogram.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    @LogExecution
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try{
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername()
                            , loginRequest.getPassword()));

            EmployeeUserPrincipal userPrincipal = (EmployeeUserPrincipal) authentication.getPrincipal();
            String token = jwtService.generateToken(userPrincipal);

            return ResponseEntity.ok(new LoginResponse(true,token,"Login successful"));

        }catch(BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, null, "Invalid username or password"));
        }
    }

    @Transactional
    @LogExecution
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();

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

        employee.setCreatedBy(currentUser.getUsername());
        employee.setUpdatedBy(currentUser.getUsername());

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDto(savedEmployee);
    }

    //@Cacheable(value = "employees", keyGenerator = "employeeCacheKeyGenerator")
    @Transactional(readOnly = true)
    @Override
    public EmployeeProfileDto getEmployeeProfile() {
        System.out.println("<--Caching started for Profile!-->");
        Employee employee = securityUtil.getAuthenticatedEmployee();
        return profileMapper.toProfileDto(employee);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getEmployeeRoles() {
        Employee employee = securityUtil.getAuthenticatedEmployee();
        return employee.getEmployeeRoles()
                .stream()
                .map(EmployeeRole::getRole_title)
                .toList();
    }

    //@CachePut(value = "employees", keyGenerator = "employeeCacheKeyGenerator")
    @Transactional
    @LogExecution
    @Override
    public EmployeeProfileDto updateEmployeeProfile(EmployeeProfileDto employeeProfile){
        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        Employee employee = employeeRepository.findById(currentUser.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Employee not editable"));

        profileMapper.updateProfileFromDto(employeeProfile, employee);

        employee.setUpdatedBy(currentUser.getUsername());

        Employee updatedProfile = employeeRepository.save(employee);
        return profileMapper.toProfileDto(updatedProfile);
    }

    //@Cacheable(value = "employeeList", key = "'page=' + (#searchRequest?.page ?: 0) + '_size=' + (#searchRequest?.size ?: 4) + '_filters=' + (#searchRequest != null ? #searchRequest.toString() : 'default')")
    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeDto> getAllEmployees(EmployeeSearchRequest searchRequest) {
        Specification<Employee> spec = EmployeeSpecification.filterEmployees(searchRequest);

        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(),
                Sort.by("id").ascending());

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        return employeePage.map(employeeMapper::mapToEmployeeDto);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists " +
                                "with given id: " + employeeId));

        return employeeMapper.mapToEmployeeDto(employee);
    }

    //@CacheEvict(value = "employeeList", allEntries = true)
    @Transactional
    @LogExecution
    @Override
    public EmployeeDto updateEmployeeById(Long employeeId, EmployeeDto updatedEmployee) {
        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        employeeMapper.updateEmployeeFromDto(updatedEmployee,employee);

        if (updatedEmployee.getEmployeeRoles() != null) {
            List<EmployeeRole> roles = updatedEmployee.getEmployeeRoles().stream()
                    .map(roleDto -> employeeRoleRepository.findById(roleDto.getId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            employee.setEmployeeRoles(roles);
        }

        employee.setUpdatedBy(currentUser.getUsername());

        Employee updatedEmployeeObj = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    //@CacheEvict(value = "employeeList", allEntries = true)
    @Transactional
    @LogExecution
    @Override
    public void deleteEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findByIdAndDeletedFalse(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exists " +
                                "with given id: "+ employeeId));
        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    //@CacheEvict(value = "employeeList", allEntries = true)
    @Override
    @Transactional
    @LogExecution
    public EmployeeDto assignRoleToEmployee(Long employeeId, List<Long> roleIds) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        Employee currentUser = securityUtil.getAuthenticatedEmployee();

        List<EmployeeRole> roles = roleIds.stream()
                .map(roleId -> employeeRoleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId)))
                .collect(Collectors.toList());

        employee.setEmployeeRoles(roles);
        employee.setUpdatedBy(currentUser.getUsername());

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.mapToEmployeeDto(updatedEmployee);
    }

}
