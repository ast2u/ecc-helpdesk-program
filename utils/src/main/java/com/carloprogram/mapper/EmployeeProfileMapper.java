package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeProfileMapper {
    EmployeeProfileDto toProfileDto(Employee employee);
}
