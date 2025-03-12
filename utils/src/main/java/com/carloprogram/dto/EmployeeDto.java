package com.carloprogram.dto;


import com.carloprogram.model.embeddable.Address;
import com.carloprogram.model.embeddable.FullName;
import com.carloprogram.model.enums.EmploymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto extends BaseDto{

    private FullName fullName;

    private String username;

    @NotNull(message = "Birth date is mandatory")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Valid
    private Address address;

    private String contactNumber;
    private EmploymentStatus employmentStatus;
    private List<EmployeeRoleDto> employeeRoles;

}
