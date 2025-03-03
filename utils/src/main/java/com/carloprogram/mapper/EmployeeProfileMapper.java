package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeProfileMapper {
    @Mapping(target = "age", expression = "java(com.carloprogram.util.employee.DateUtil.computeAge(employee.getBirthDate()))")
    EmployeeProfileDto toProfileDto(Employee employee);
}
