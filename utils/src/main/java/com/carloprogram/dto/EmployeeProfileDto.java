package com.carloprogram.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileDto implements Serializable {
    @Size(min = 1, message = "First name cannot be empty")
    private String firstName;

    @Size(min = 1, message = "Last name cannot be empty")
    private String lastName;

    @Size(min = 1, message = "Username cannot be empty")
    private String username;

    @PastOrPresent(message = "Birthdate cannot be in the future")
    private LocalDate birthDate;

    private int age;

    @Size(min = 1, message = "Address cannot be empty")
    private String address;

    @Size(min = 1, message = "Contact number cannot be empty")
    private String contactNumber;
}
