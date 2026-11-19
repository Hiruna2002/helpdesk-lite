
package com.helpdesklite.entity;

import com.helpdesklite.enums.Priority;
import com.helpdesklite.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "idx_ticket_status", columnList = "status"),
        @Index(name = "idx_ticket_priority", columnList = "priority"),
        @Index(name = "idx_ticket_created_at", columnList = "created_at")
})
public class Ticket {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Column(nullable = false, length = 120)
 private String title;

 @Column(nullable = false, columnDefinition = "TEXT")
 private String description;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 20)
 private Priority priority;

 @Enumerated(EnumType.STRING)
 @Column(nullable = false, length = 20)
 private TicketStatus status = TicketStatus.OPEN;

 @Column(name = "requester_name", nullable = false, length = 100)
 private String requesterName;

 @Column(name = "requester_email", nullable = false, length = 180)
 private String requesterEmail;

 @Column(name = "assignee_ref", length = 180)
 private String assigneeRef;

 @Column(name = "created_at", nullable = false)
 private LocalDateTime createdAt;

 @Column(name = "updated_at", nullable = false)
 private LocalDateTime updatedAt;

 @Column(name = "resolved_at")
 private LocalDateTime resolvedAt;

 @PrePersist
 public void onCreate() {
  LocalDateTime now = LocalDateTime.now();
  createdAt = now;
  updatedAt = now;

  if (status == null) {
   status = TicketStatus.OPEN;
  }
 }

 @PreUpdate
 public void onUpdate() {
  updatedAt = LocalDateTime.now();
 }
}
