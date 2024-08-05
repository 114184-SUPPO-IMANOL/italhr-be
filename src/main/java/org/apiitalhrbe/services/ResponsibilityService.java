package org.apiitalhrbe.services;

import org.apiitalhrbe.dtos.request.WorkstationRequestDTO;
import org.apiitalhrbe.entities.nosql.ResponsibilityEntity;
import org.apiitalhrbe.repositories.nosql.ResponsibilityRepository;
import org.apiitalhrbe.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResponsibilityService {

    @Autowired
    private ResponsibilityRepository responsibilityRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public ResponsibilityEntity getResponsibilityById(UUID id) {
        return responsibilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Responsibility not found"));
    }

    public UUID createResponsibility(List<String> responsibilities) {
        ResponsibilityEntity responsibilityEntity = new ResponsibilityEntity();
        responsibilityEntity.setId(UUID.randomUUID());
        responsibilityEntity.setResponsibilities(responsibilities);
        ResponsibilityEntity responsibilitySaved = responsibilityRepository.insert(responsibilityEntity);
        return responsibilitySaved.getId();
    }

    public UUID updateResponsibility(UUID id, List<String> responsibilities) {
        ResponsibilityEntity responsibilityEntity = getResponsibilityById(id);
        responsibilityEntity.setResponsibilities(responsibilities);
        ResponsibilityEntity responsibilitySaved = responsibilityRepository.save(responsibilityEntity);
        return responsibilitySaved.getId();
    }

}
