package org.apiitalhrbe.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public record PersonRequestDTO(

        @JsonProperty("first_name")
                @NotNull(message = "First name is required")
                @NotEmpty(message = "First name cant be empty")
                @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @JsonProperty("last_name")
                @NotNull(message = "Last name is required")
                @NotEmpty(message = "Last name cant be empty")
                @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @JsonProperty("document_type")
                @NotNull(message = "Document type is required")
                @NotEmpty(message = "Document type cant be empty")
                @Size(min = 2, max = 50, message = "Document type must be between 2 and 50 characters")
        String documentType,

        @JsonProperty("document_number")
                @NotNull(message = "Document number is required")
                @NotEmpty(message = "Document number cant be empty")
        String documentNumber,

        @JsonProperty("gender")
        String gender,

        @JsonProperty("marital_status")
        String maritalStatus,

        @JsonProperty("nationality")
        String nationality,

        @JsonProperty("birth_date")
                @NotNull(message = "Birth date is required")
                @NotEmpty(message = "Birth date cant be empty")
        LocalDate birthDate,

        @JsonProperty("profile_picture")
        StoredDocumentRequestDTO profilePicture
) {
}
