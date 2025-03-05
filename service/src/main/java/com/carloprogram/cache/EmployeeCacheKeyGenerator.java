package com.carloprogram.cache;

import com.carloprogram.impl.EmployeeServiceImpl;
import com.carloprogram.model.Employee;
import com.carloprogram.security.config.SecurityUtil;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("employeeCacheKeyGenerator")
public class EmployeeCacheKeyGenerator implements KeyGenerator {

    private final SecurityUtil securityUtil;

    public EmployeeCacheKeyGenerator(SecurityUtil securityUtil){
        this.securityUtil = securityUtil;
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        Employee employeeId = securityUtil.getAuthenticatedEmployee();
        return employeeId.getId();
    }
}
