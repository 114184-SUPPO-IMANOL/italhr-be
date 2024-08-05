package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;


public record HandbookRequestDTO(
        @JsonProperty("objectives")
        @NotNull(message = "Objectives is required")
        @NotEmpty(message = "Objectives is required")
        String objectives,
        @JsonProperty("responsibilities")
        @NotNull(message = "Responsibilities is required")
        @NotEmpty(message = "Responsibilities is required")
        @Size(min = 1, message = "Responsibilities must have at least one element")
        List<String> responsibilities,
        @JsonProperty("functions")
        @NotNull(message = "Functions is required")
        @NotEmpty(message = "Functions is required")
        @Size(min = 1, message = "Functions must have at least one element")
        List<String> functions,
        @JsonProperty("required_profile")
        RequiredProfileRequestDTO requiredProfile,
        @JsonProperty("skills")
        SkillRequestDTO skill
) {
}
