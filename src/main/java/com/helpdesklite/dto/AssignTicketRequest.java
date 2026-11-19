package com.helpdesklite.dto;
import jakarta.validation.constraints.*;
public record AssignTicketRequest(
        @NotBlank(message="Assignee reference is required")
        @Size(max=180) String assigneeRef) {}
