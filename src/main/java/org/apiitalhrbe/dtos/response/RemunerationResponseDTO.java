package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemunerationResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("monthly_salary")
        Integer monthlySalary,
        @JsonProperty("annual_salary")
        Integer annualSalary,
        @JsonProperty("comments")
        String comments
) {

}
