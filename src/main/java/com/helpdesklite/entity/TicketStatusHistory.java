
package com.helpdesklite.entity;

import com.helpdesklite.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "ticket_status_history",
        indexes = @Index(
                name = "idx_history_ticket_changed",
                columnList = "ticket_id, changed_at"
        )
)
public class TicketStatusHistory {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 @JoinColumn(
         name = "ticket_id",
         nullable = false,
         foreignKey = @ForeignKey(name = "fk_history_ticket")
 )
 private Ticket ticket;

 @Enumerated(EnumType.STRING)
 @Column(name = "old_status", length = 20)
 private TicketStatus oldStatus;

 @Enumerated(EnumType.STRING)
 @Column(name = "new_status", nullable = false, length = 20)
 private TicketStatus newStatus;

 @Column(name = "actor_ref", nullable = false, length = 180)
 private String actorRef;

 @Column(name = "changed_at", nullable = false)
 private LocalDateTime changedAt;

 protected TicketStatusHistory() {
 }

 public TicketStatusHistory(
         Ticket ticket,
         TicketStatus oldStatus,
         TicketStatus newStatus,
         String actorRef
 ) {
  this.ticket = ticket;
  this.oldStatus = oldStatus;
  this.newStatus = newStatus;
  this.actorRef = actorRef;
  this.changedAt = LocalDateTime.now();
 }

 public Long getTicketId() {
  return ticket.getId();
 }
}
