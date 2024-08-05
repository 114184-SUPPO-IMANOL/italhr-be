package org.apiitalhrbe.controllers;

import org.apiitalhrbe.dtos.request.StoredDocumentRequestDTO;
import org.apiitalhrbe.dtos.response.StoredDocumentResponseDTO;
import org.apiitalhrbe.services.StoredDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stored-documents")
@CrossOrigin(origins = "http://localhost:4200")
public class StoredDocumentController {

    @Autowired
    private StoredDocumentService storedDocumentService;


    @PostMapping
    public ResponseEntity<StoredDocumentResponseDTO> save(@ModelAttribute StoredDocumentRequestDTO storedDocumentRequestDTO) {
        return ResponseEntity.ok(storedDocumentService.save(storedDocumentRequestDTO));
    }

}
