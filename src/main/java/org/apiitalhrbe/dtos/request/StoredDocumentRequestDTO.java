package org.apiitalhrbe.dtos.request;

import org.springframework.web.multipart.MultipartFile;

public record StoredDocumentRequestDTO(
        Long id,
        String name,
        String fileName,
        String extension,
        String status,
        Boolean deleted,
        MultipartFile file,
        String urlFile
) {
}
