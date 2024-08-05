package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChiefRemarksResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("members_team_quantity")
        Integer membersTeamQuantity,
        @JsonProperty("estimated_effect")
        String estimatedEffect
) {
}
