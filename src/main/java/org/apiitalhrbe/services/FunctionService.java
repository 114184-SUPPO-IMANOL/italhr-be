package org.apiitalhrbe.services;

import org.apiitalhrbe.dtos.request.WorkstationRequestDTO;
import org.apiitalhrbe.entities.nosql.FunctionEntity;
import org.apiitalhrbe.repositories.nosql.FunctionRepository;
import org.apiitalhrbe.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FunctionService {

    @Autowired
    private FunctionRepository functionRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public FunctionEntity getFunctionById(UUID id) {
        return functionRepository.findById(id).orElseThrow(() -> new RuntimeException("Function not found"));
    }

    public UUID createFunction(List<String> functions) {
        FunctionEntity functionEntity = new FunctionEntity();
        functionEntity.setId(UUID.randomUUID());
        functionEntity.setFunctions(functions);
        FunctionEntity functionSaved = functionRepository.insert(functionEntity);
        return functionSaved.getId();
    }

    public UUID updateFunction(UUID id, List<String> functions) {
        FunctionEntity functionEntity = getFunctionById(id);
        functionEntity.setFunctions(functions);
        FunctionEntity functionSaved = functionRepository.save(functionEntity);
        return functionSaved.getId();
    }
}
