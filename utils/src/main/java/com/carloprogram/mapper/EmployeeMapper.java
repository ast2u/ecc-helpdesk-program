package com.carloprogram.mapper;

import com.carloprogram.model.Employee;
import com.carloprogram.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.security.SecureRandom;

@Mapper(componentModel = "spring", uses = EmployeeRoleMapper.class)
public interface EmployeeMapper {

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "mapEmployeeToId")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "mapEmployeeToId")
    EmployeeDto mapToEmployeeDto(Employee employee);

    @Mapping(target = "username", source = "lastName",  qualifiedByName = "generateUsername")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Employee mapToEmployee(EmployeeDto employeeDto);

    @Named("generateUsername")
    static String generateUsername(String lastName) {
        SecureRandom random = new SecureRandom();
        int randomN = 100 + random.nextInt(900);
        String halfLastName = lastName.substring(0, (lastName.length() + 1) / 2).toLowerCase();
        return halfLastName + randomN;
    }

    @Named("mapEmployeeToId")
    static Long mapEmployeeToId(Employee employee) {
        return (employee != null) ? employee.getId() : null;
    }
}
