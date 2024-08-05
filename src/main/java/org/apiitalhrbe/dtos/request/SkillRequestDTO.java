package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SkillRequestDTO(
        @JsonProperty("social_skills")
        String socialSkills,
        @JsonProperty("manual_skills")
        String manualSkills,
        @JsonProperty("mental_skills")
        String mentalSkills,
        @JsonProperty("change_flexibilities")
        String changeFlexibilities,
        @JsonProperty("service_vocation")
        String serviceVocation,
        @JsonProperty("analytical_skills")
        String analyticalSkills

) {
}
