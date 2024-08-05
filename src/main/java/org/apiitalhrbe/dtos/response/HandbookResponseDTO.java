package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record HandbookResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("objectives")
        String objectives,
        @JsonProperty("responsibility_id")
        UUID responsibilityId,
        @JsonProperty("function_id")
        UUID functionId,
        @JsonProperty("required_perfil_id")
        Long requiredPerfilId,
        @JsonProperty("skill_id")
        Long skillId
) {
}
