package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WorkstationResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("handbook")
        HandbookResponseDTO handbook,
        @JsonProperty("dependents")
        Integer dependents,
        @JsonProperty("is_active")
        Boolean isActive
) {
}
