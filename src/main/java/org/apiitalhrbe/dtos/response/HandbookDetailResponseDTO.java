package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandbookDetailResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("objectives")
    private String objectives;

    @JsonProperty("responsibilities")
    private List<String> responsibilities;

    @JsonProperty("functions")
    private List<String> functions;

    @JsonProperty("required_profile")
    private RequiredProfileResponseDTO requiredProfile;

    @JsonProperty("skills")
    private SkillResponseDTO skill;
}
