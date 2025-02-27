package com.carloprogram.dto;


import com.carloprogram.model.enums.EmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String contactNumber;
    private EmploymentStatus employmentStatus;
    private List<EmployeeRoleDto> employeeRoleIds = new ArrayList<>();

}
