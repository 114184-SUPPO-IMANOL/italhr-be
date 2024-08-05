package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record RemunerationRequestDTO(

        @JsonProperty("monthly_salary")
                @NotNull(message = "Monthly salary is required")
                @Min(value = 1, message = "Monthly salary must be greater than 0")
        Integer monthlySalary,

        @JsonProperty("annual_salary")
                @NotNull(message = "Annual salary is required")
                @Min(value = 1, message = "Annual salary must be greater than 0")
        Integer annualSalary,

        @JsonProperty("comments")
        String comments
) {
}
