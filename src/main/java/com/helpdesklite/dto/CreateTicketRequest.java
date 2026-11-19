package com.helpdesklite.dto;
import com.helpdesklite.enums.Priority;
import jakarta.validation.constraints.*;
public record CreateTicketRequest(
 @NotBlank(message="Title is required") @Size(max=120, message="Title must not exceed 120 characters") String title,
 @NotBlank(message="Description is required") @Size(max=5000, message="Description must not exceed 5000 characters") String description,
 @NotNull(message="Priority is required") Priority priority,
 @NotBlank(message="Requester name is required") @Size(max=100, message="Requester name must not exceed 100 characters") String requesterName,
 @NotBlank(message="Requester email is required") @Email(message="Requester email must be valid") @Size(max=180) String requesterEmail
) {}
