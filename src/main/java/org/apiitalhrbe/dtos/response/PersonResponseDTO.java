package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record PersonResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("document_type")
        String documentType,
        @JsonProperty("document_number")
        String documentNumber,
        @JsonProperty("gender")
        String gender,
        @JsonProperty("marital_status")
        String maritalStatus,
        @JsonProperty("birth_date")
        LocalDate birthDate,
        @JsonProperty("nationality")
        String nationality,
        @JsonProperty("is_active")
        Boolean isActive,
        @JsonProperty("profile_picture")
        StoredDocumentResponseDTO profilePicture
) {
}
