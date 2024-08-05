package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record OtherRemarksRequestDTO(

        @JsonProperty("manager")
                @NotNull(message = "The manager is required.")
                @NotEmpty(message = "The manager cant be empty.")
                @Size(max = 255, message = "The manager must be up to 255 characters.")
        String manager,

        @JsonProperty("request_received")
        LocalDate requestReceived,

        @JsonProperty("comments")
        String comments
) {
}
