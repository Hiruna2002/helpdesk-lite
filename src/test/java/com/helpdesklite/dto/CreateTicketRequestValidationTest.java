package com.helpdesklite.dto;

import com.helpdesklite.enums.Priority;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreateTicketRequestValidationTest {
    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsInvalidRequesterEmail() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login problem",
                "Cannot access the employee portal",
                Priority.HIGH,
                "Test User",
                "not-an-email"
        );

        var violations = validator.validate(request);

        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("requesterEmail")));
    }
}
