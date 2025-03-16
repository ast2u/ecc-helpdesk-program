package com.carloprogram.dto.search;

import com.carloprogram.model.enums.EmploymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeSearchRequest extends BaseSearchRequest {
    private String name;
    private LocalDate birthDate;
    private String houseNumber;
    private String street;
    private String city;
    private String zipCode;
    private EmploymentStatus status;
    private String roles;



}
