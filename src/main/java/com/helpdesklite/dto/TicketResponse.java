package com.helpdesklite.dto;
import com.helpdesklite.entity.Ticket;
import com.helpdesklite.enums.*;
import java.time.LocalDateTime;
public record TicketResponse(
        Long id,
        String title,
        String description,
        Priority priority,
        TicketStatus status,
        String requesterName,
        String requesterEmail,
        String assigneeRef,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime resolvedAt
)
{
 public static TicketResponse from(Ticket t){
  return new TicketResponse(
          t.getId(),
          t.getTitle(),
          t.getDescription(),
          t.getPriority(),
          t.getStatus(),
          t.getRequesterName(),
          t.getRequesterEmail(),
          t.getAssigneeRef(),
          t.getCreatedAt(),
          t.getUpdatedAt(),
          t.getResolvedAt());
 }
}
