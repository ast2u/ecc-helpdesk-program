package com.carloprogram.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee_role")
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_title")
    private String role_title;

    @Column(name = "role_desc")
    private String role_description;

    @ManyToMany(mappedBy = "employeeRoles")
    private Set<Employee> employees = new HashSet<>();

}
