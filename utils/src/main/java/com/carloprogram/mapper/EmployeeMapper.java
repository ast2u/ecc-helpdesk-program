package com.carloprogram.mapper;

import com.carloprogram.model.Employee;
import com.carloprogram.dto.EmployeeDto;
import com.carloprogram.model.embeddable.FullName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.security.SecureRandom;

@Mapper(componentModel = "spring", uses = EmployeeRoleMapper.class)
public interface EmployeeMapper {
    EmployeeDto mapToEmployeeDto(Employee employee);

    @Mapping(target = "username", source = "fullName", qualifiedByName = "generateUsername")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Employee mapToEmployee(EmployeeDto employeeDto);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "employeeRoles", ignore = true)
    void updateEmployeeFromDto(EmployeeDto dto, @MappingTarget Employee employee);

    @Named("generateUsername")
    static String generateUsername(FullName fullName) {
        SecureRandom random = new SecureRandom();
        int randomN = 100 + random.nextInt(900);
        String halfLastName = fullName.getLastName().substring(0, (fullName.getLastName().length() + 1) / 2).toLowerCase();
        return halfLastName + randomN;
    }
}
