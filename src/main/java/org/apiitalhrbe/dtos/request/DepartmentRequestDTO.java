package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DepartmentRequestDTO(
        @JsonProperty("name")
        String name,
        @JsonProperty("upper_department_id")
        Long upperDepartmentId,
        @JsonProperty("chief_id")
        Long chiefId,
        @JsonProperty("capacity")
        Integer capacity
) {
}
