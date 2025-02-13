package com.carloprogram.dto;


import com.carloprogram.model.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String address;
    private String contactNumber;
    private String employmentStatus;
    private EmployeeRole employeeRoleId; //fix

}
