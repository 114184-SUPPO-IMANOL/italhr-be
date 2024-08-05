package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record RequiredProfileRequestDTO(
        @JsonProperty("age_category")
        @Min(value = 1, message = "Age category must be positive")
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
