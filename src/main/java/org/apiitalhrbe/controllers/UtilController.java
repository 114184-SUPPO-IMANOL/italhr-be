package org.apiitalhrbe.controllers;

import org.apiitalhrbe.services.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UtilController {

    @Autowired
    private UtilService utilService;

    @GetMapping("/nationalities")
    public ResponseEntity<List<String>> getNationalities() {
        return ResponseEntity.ok(utilService.getNationalities());
    }

    @GetMapping("/document-types")
    public ResponseEntity<List<String>> getDocumentTypes() {
        return ResponseEntity.ok(utilService.getDocumentTypes());
    }
}
