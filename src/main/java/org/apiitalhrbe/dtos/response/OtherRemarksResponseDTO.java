package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record OtherRemarksResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("manager")
        String manager,
        @JsonProperty("request_received")
        LocalDate requestReceived,
        @JsonProperty("comments")
        String comments
) {
}
