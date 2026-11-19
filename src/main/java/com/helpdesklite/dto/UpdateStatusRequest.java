package com.helpdesklite.dto;
import com.helpdesklite.enums.TicketStatus;
import jakarta.validation.constraints.*;
public record UpdateStatusRequest(@NotNull(message="Status is required") TicketStatus status,
 @NotBlank(message="Actor reference is required") @Size(max=180) String actorRef) {}
