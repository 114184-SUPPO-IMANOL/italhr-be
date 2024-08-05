package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRequestDTO(
        @JsonProperty("password")
        String password,
        @JsonProperty("email")
        String email,
        @JsonProperty("role")
        String role,
        @JsonProperty("employee_id")
        Long employeeId
) {
}
