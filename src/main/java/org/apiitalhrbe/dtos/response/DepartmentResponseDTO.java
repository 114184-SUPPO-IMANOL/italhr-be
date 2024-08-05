package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record DepartmentResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("parent_department_id")
        Long parentDepartmentId,
        @JsonProperty("chief_id")
        Long chiefId,
        @JsonProperty("capacity")
        Integer capacity,
        @JsonProperty("created_at")
        LocalDate createdAt,
        @JsonProperty("updated_at")
        LocalDate updatedAt,
        @JsonProperty("is_active")
        Boolean isActive) {
}
