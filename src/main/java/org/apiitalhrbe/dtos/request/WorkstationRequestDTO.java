package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WorkstationRequestDTO(
        @JsonProperty("name")
        @NotNull(message = "Name is required")
        @NotEmpty(message = "Name is required")
        String name,
        @JsonProperty("handbook")
        @NotNull(message = "Handbook is required")
        HandbookRequestDTO handbook,
        @JsonProperty("dependents")
        @NotNull(message = "Dependents is required")
        @Min(value = 1, message = "Dependents must be positive")
        Integer dependents
) {
}
