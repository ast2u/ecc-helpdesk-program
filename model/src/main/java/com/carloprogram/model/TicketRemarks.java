package com.carloprogram.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_remarks")
public class TicketRemarks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false) // Many Remarks to One Ticket
    private HelpTicket ticketId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false) // Many Remarks to One Employee
    private Employee employeeId;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;
}
