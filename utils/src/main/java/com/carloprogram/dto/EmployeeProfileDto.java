package com.carloprogram.dto;

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
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthDate;
    private int age;
    private String address;
    private String contactNumber;
    //private List<HelpTicket> createdTickets;
}
