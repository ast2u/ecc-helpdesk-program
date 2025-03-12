package com.carloprogram.dto;

import com.carloprogram.model.embeddable.Address;
import com.carloprogram.model.embeddable.FullName;
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
    private FullName fullName;

    @Size(min = 1, message = "Username cannot be empty")
    private String username;

    @PastOrPresent(message = "Birthdate cannot be in the future")
    private LocalDate birthDate;

    private int age;

    private Address address;

    @Size(min = 1, message = "Contact number cannot be empty")
    private String contactNumber;
}
