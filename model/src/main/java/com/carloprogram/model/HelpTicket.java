package com.carloprogram.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name = "help_ticket")
public class HelpTicket {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticket_number;

    //@Column(name = "title", nullable = false)
    private String ticket_title;

    //@Column(name = "body", nullable = false)
    private String body;


    private String assignee;

    //@Column(name = "status", nullable = false)
    private String status;
    private Date created_date;
    private String created_by;
    private Date updated_date;
    private String updated_by;
    private String remarks;

}
