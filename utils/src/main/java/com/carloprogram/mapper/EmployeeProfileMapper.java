package com.carloprogram.mapper;

import com.carloprogram.dto.EmployeeProfileDto;
import com.carloprogram.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeProfileMapper {
    @Mapping(target = "age", source = "birthDate", qualifiedByName = "computeAge")
    EmployeeProfileDto toProfileDto(Employee employee);

    void updateProfileFromDto(EmployeeProfileDto dto, @MappingTarget Employee employee);

    @Named("computeAge")
    static int computeAge(LocalDate birthDate) {
        return (birthDate == null) ? 0 : Period.between(birthDate, LocalDate.now()).getYears();
    }
}
