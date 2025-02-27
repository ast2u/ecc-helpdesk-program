package com.carloprogram.model;

import com.carloprogram.model.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false)
    private EmploymentStatus employmentStatus;

    @ManyToMany
    @JoinTable (
            name = "employee_to_role",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_role_id")
    )
    private List<EmployeeRole> employeeRoles = new ArrayList<>();

    @OneToMany(mappedBy = "assignee")
    private Set<HelpTicket> assignedTickets = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<HelpTicket> createdTickets = new HashSet<>();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false; // Default: Not deleted

    public void removeRole(EmployeeRole role){
        if(employeeRoles != null){
            this.employeeRoles.remove(role);
        }
    }


}
