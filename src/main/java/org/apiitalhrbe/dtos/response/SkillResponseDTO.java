package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SkillResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("social_skills")
        String socialSkills,
        @JsonProperty("manual_skills")
        String manualSkills,
        @JsonProperty("mental_skills")
        String mentalSkills,
        @JsonProperty("change_flexibility")
        String changeFlexibility,
        @JsonProperty("service_vocation")
        String serviceVocation,
        @JsonProperty("analytical_skills")
        String analyticalSkills
) {
}
