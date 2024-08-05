package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record AuthorizationResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("request_received")
        LocalDate requestReceived,
        @JsonProperty("comments")
        String comments,
        @JsonProperty("is_authorized")
        Boolean isAuthorized
) {
}
