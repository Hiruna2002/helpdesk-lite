package com.helpdesklite.dto;
import com.helpdesklite.entity.TicketStatusHistory;
import com.helpdesklite.enums.TicketStatus;
import java.time.LocalDateTime;
public record HistoryResponse(
        Long id,
        Long ticketId,
        TicketStatus oldStatus,
        TicketStatus newStatus,
        String actorRef,
        LocalDateTime changedAt
)
{
 public static HistoryResponse from(TicketStatusHistory h){
  return new HistoryResponse(
          h.getId(),
          h.getTicketId(),
          h.getOldStatus(),
          h.getNewStatus(),
          h.getActorRef(),
          h.getChangedAt()
  );
 }
}
