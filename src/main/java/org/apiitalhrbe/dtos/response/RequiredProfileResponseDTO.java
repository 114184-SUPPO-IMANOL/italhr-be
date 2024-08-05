package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequiredProfileResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("age_category")
        Integer ageCategory,
        @JsonProperty("gender")
        String gender,
        @JsonProperty("place_residency")
        String placeResidency,
        @JsonProperty("time_availability")
        String timeAvailability,
        @JsonProperty("contract_type")
        String contractType,
        @JsonProperty("minimal_education")
        String minimalEducation,
        @JsonProperty("title")
        String title,
        @JsonProperty("experience")
        String experience
) {
}
