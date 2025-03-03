package com.carloprogram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    //private String password;
    private LocalDate birthDate;
    private String address;
    private String contactNumber;
    //private List<HelpTicket> createdTickets;
}
