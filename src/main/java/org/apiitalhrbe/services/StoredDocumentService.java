package org.apiitalhrbe.services;

import jakarta.servlet.http.HttpServletRequest;
import org.apiitalhrbe.dtos.request.StoredDocumentRequestDTO;
import org.apiitalhrbe.dtos.response.StoredDocumentResponseDTO;
import org.apiitalhrbe.entities.sql.StoredDocumentEntity;
import org.apiitalhrbe.repositories.sql.StoredDocumentRepository;
import org.apiitalhrbe.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class StoredDocumentService {

    @Autowired
    private StoredDocumentRepository storedDocumentRepository;
    
    @Autowired
    private FileStorageService fileStorageService;

    private final ModelMapper modelMapper = new ModelMapper();

    public StoredDocumentResponseDTO save(StoredDocumentRequestDTO storedDocumentRequestDTO) {

        StoredDocumentEntity storedDocument = modelMapper.map(storedDocumentRequestDTO, StoredDocumentEntity.class);

        Long id = storedDocument.getId() != null ? storedDocument.getId() : 0;


        String originalFilename = storedDocument.getFile().getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = fileStorageService.storeFile(storedDocument.getFile(), "");

        storedDocument.setFileName(fileName);
        storedDocument.setExtension(extension);

        StoredDocumentEntity storedDocumentSaved = storedDocumentRepository.save(storedDocument);
        return modelMapper.map(storedDocumentSaved, StoredDocumentResponseDTO.class);
    }

    public Resource download(String completefileName, HttpServletRequest request) {
        return fileStorageService.loadResource(completefileName);
    }

    public Resource downloadByFileName(String fileName, HttpServletRequest request) {
        // TODO: implements logic
        return null;
    }

    public Boolean deleteById(Long id) {
        // TODO: implements logic
        return null;
    }

}
