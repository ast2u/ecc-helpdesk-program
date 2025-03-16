package com.carloprogram.model;

import com.carloprogram.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "help_tickets")
public class HelpTicket extends BaseEntity{

    @Column(name = "ticket_number", unique = true, nullable = false, updatable = false)
    private String ticketNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Employee assignee;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketRemarks> remarks = new ArrayList<>();

}
