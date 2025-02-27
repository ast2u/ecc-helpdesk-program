package com.carloprogram.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRoleDto {
    private Long id;

    @NotBlank(message = "Role title is mandatory")
    private String role_title;

    @NotBlank(message = "Role description is mandatory")
    private String role_description;
}
