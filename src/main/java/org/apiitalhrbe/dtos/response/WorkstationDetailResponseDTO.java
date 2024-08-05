package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WorkstationDetailResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("handbook")
        HandbookDetailResponseDTO handbook,
        @JsonProperty("dependents")
        Integer dependents,
        @JsonProperty("is_active")
        Boolean isActive) {
}
