package com.carloprogram.model;
import com.carloprogram.model.enums.TicketStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "help_tickets")
public class HelpTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", unique = true, nullable = false, updatable = false)
    private String ticketNumber;

    @Column(nullable = false)
    private String ticketTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    private TicketStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private Employee assignee;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Employee updatedBy;

    @OneToMany(mappedBy = "ticketId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketRemarks> remarks = new ArrayList<>();

    @PrePersist
    public void generateTicketNumber() {
        if (this.ticketNumber == null) {
            this.ticketNumber = UUID.randomUUID().toString();
        }
    }

}
