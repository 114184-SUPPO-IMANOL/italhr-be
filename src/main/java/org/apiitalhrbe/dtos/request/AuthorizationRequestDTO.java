package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record AuthorizationRequestDTO(

        @JsonProperty("request_received")
                @NotNull(message = "Request received is required")
                @NotBlank(message = "Request received cant be empty")
        LocalDate requestReceived,

        @JsonProperty("comments")
        String comments,

        @JsonProperty("is_authorized")
                @NotNull(message = "Is authorized is required")
        Boolean isAuthorized
) {
}
