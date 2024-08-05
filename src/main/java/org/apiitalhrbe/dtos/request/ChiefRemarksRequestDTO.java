package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record ChiefRemarksRequestDTO(

        @JsonProperty("members_team_quantity")
                @NotNull(message = "The members team quantity is required.")
                @Min(value = 1, message = "The members team quantity must be greater than 0.")
        Integer membersTeamQuantity,

        @JsonProperty("estimated_effect")
        String estimatedEffect
) {
}
