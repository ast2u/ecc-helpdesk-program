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
    private String address;
    private EmploymentStatus status;
    private List<Long> roles;



}
