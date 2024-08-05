package org.apiitalhrbe.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StoredDocumentResponseDTO(
        @JsonProperty("id")
        Long id,
        @JsonProperty("name")
        String name,
        @JsonProperty("file_name")
        String fileName,
        @JsonProperty("extension")
        String extension,
        @JsonProperty("status")
        String status,
        @JsonProperty("deleted")
        Boolean deleted,
        @JsonProperty("url_file")
        String urlFile
) {
}
