package org.apiitalhrbe.services;

import jakarta.persistence.EntityNotFoundException;
import org.apiitalhrbe.dtos.request.WorkstationRequestDTO;
import org.apiitalhrbe.dtos.response.WorkstationDetailResponseDTO;
import org.apiitalhrbe.dtos.response.WorkstationResponseDTO;
import org.apiitalhrbe.entities.nosql.*;
import org.apiitalhrbe.entities.sql.WorkstationEntity;
import org.apiitalhrbe.repositories.nosql.HardcodedHistoryRepository;
import org.apiitalhrbe.repositories.nosql.WorkstationHistoryRepository;
import org.apiitalhrbe.repositories.sql.WorkstationRepository;
import org.apiitalhrbe.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WorkstationService {

    @Autowired
    private WorkstationRepository workstationRepository;

    @Autowired
    private ResponsibilityService responsibilityService;

    @Autowired
    private ReportService<WorkstationHistoryEntity> reportService;

    @Autowired
    private WorkstationHistoryRepository workstationHistoryRepository;

    @Autowired
    private HardcodedHistoryRepository hardcodedHistoryRepository;

    @Autowired
    private FunctionService functionService;

    private final ModelMapper modelMapper = new ModelMapper();

    public WorkstationResponseDTO getWorkstationById(Long id) {
        Optional<WorkstationEntity> workstationEntity = workstationRepository.findById(id);
        if (workstationEntity.isEmpty()) {
            throw new RuntimeException("Workstation not found");
        }
        return modelMapper.map(workstationEntity.get(), WorkstationResponseDTO.class);
    }

    public WorkstationDetailResponseDTO getWorkstationDetailById(Long id) {
        WorkstationEntity workstation = workstationRepository.findById(id).orElseThrow(() -> new RuntimeException("Workstation not found"));
        ResponsibilityEntity responsibility = responsibilityService.getResponsibilityById(workstation.getHandbook().getResponsibilityId());
        FunctionEntity function = functionService.getFunctionById(workstation.getHandbook().getFunctionId());
        WorkstationDetailResponseDTO workstationDetailResponseDTO = modelMapper.map(workstation, WorkstationDetailResponseDTO.class);
        workstationDetailResponseDTO.handbook().setResponsibilities(responsibility.getResponsibilities());
        workstationDetailResponseDTO.handbook().setFunctions(function.getFunctions());
        return workstationDetailResponseDTO;
    }

    public List<WorkstationResponseDTO> getAllWorkstations() {
        List<WorkstationEntity> workstationEntities = workstationRepository.findAll();
        return workstationEntities.stream()
                .map(workstationEntity -> modelMapper.map(workstationEntity, WorkstationResponseDTO.class))
                .toList();
    }

    public List<WorkstationResponseDTO> getAllActiveWorkstations() {
        List<WorkstationEntity> workstationEntities = workstationRepository.findAll();
        return workstationEntities.stream()
                .filter(WorkstationEntity::getIsActive)
                .map(workstationEntity -> modelMapper.map(workstationEntity, WorkstationResponseDTO.class))
                .toList();
    }

    public WorkstationResponseDTO createWorkstation(WorkstationRequestDTO workstationRequestDTO) {
        if (isValid(workstationRequestDTO)) {
            WorkstationEntity workstationEntity = modelMapper.map(workstationRequestDTO, WorkstationEntity.class);
            workstationEntity.getHandbook().setResponsibilityId(
                    responsibilityService.createResponsibility(workstationRequestDTO.handbook().responsibilities()));
            workstationEntity.getHandbook().setFunctionId(
                    functionService.createFunction(workstationRequestDTO.handbook().functions()));
            workstationEntity.setCreatedAt(LocalDate.now());
            workstationEntity.setIsActive(true);
            UUID workstationHistoryId = saveWorkstationHistory(workstationEntity, "CREATED", workstationEntity.getCreatedAt(), LocalDate.now());
            if (workstationHistoryId == null) {
                throw new RuntimeException("Error saving workstation history");
            }
            WorkstationEntity workstationSaved = workstationRepository.save(workstationEntity);
            return modelMapper.map(workstationSaved, WorkstationResponseDTO.class);
        }
        throw new IllegalArgumentException("Invalid workstation data");
    }

    public WorkstationResponseDTO updateWorkstation(WorkstationRequestDTO workstationRequestDTO, Long id) {
        WorkstationEntity workstationEntity = workstationRepository.findById(id).orElseThrow( () -> new RuntimeException("Workstation not found"));
        WorkstationEntity workstationEntityToUpdate = modelMapper.map(workstationRequestDTO, WorkstationEntity.class);
        workstationEntityToUpdate.setId(workstationEntity.getId());
        workstationEntityToUpdate.getHandbook().setResponsibilityId(
                responsibilityService.updateResponsibility(workstationEntity.getHandbook().getResponsibilityId(), workstationRequestDTO.handbook().responsibilities()));
        workstationEntityToUpdate.getHandbook().setFunctionId(
                functionService.updateFunction(workstationEntity.getHandbook().getFunctionId(), workstationRequestDTO.handbook().functions()));
        workstationEntityToUpdate.setCreatedAt(workstationEntity.getCreatedAt());
        workstationEntityToUpdate.setIsActive(workstationEntity.getIsActive());
        workstationEntityToUpdate.setUpdatedAt(LocalDate.now());
        UUID workstationHistoryId = saveWorkstationHistory(workstationEntity, "UPDATED", workstationEntity.getUpdatedAt(), LocalDate.now());
        if (workstationHistoryId == null) {
            throw new RuntimeException("Error saving workstation history");
        }
        WorkstationEntity workstationUpdated = workstationRepository.save(workstationEntityToUpdate);
        return modelMapper.map(workstationUpdated, WorkstationResponseDTO.class);
    }

    public Long deleteWorkstation(Long id) {
        WorkstationEntity workstationEntity = workstationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Workstation not found"));
        workstationEntity.setIsActive(false);
        workstationEntity.setUpdatedAt(LocalDate.now());
        UUID workstationHistoryId = saveWorkstationHistory(workstationEntity, "DELETED", workstationEntity.getUpdatedAt(), null);
        if (workstationHistoryId == null) {
            throw new RuntimeException("Error saving workstation history");
        }
        return workstationRepository.save(workstationEntity).getId();
    }

    public Long activateWorkstation(Long id) {
        WorkstationEntity workstationEntity = workstationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Workstation not found"));
        workstationEntity.setIsActive(true);
        workstationEntity.setUpdatedAt(LocalDate.now());
        UUID workstationHistoryId = saveWorkstationHistory(workstationEntity, "ACTIVATED", workstationEntity.getUpdatedAt(), null);
        if (workstationHistoryId == null) {
            throw new RuntimeException("Error saving workstation history");
        }
        return workstationRepository.save(workstationEntity).getId();
    }

    private UUID saveWorkstationHistory(WorkstationEntity workstationEntity, String state, LocalDate from, LocalDate to) {
        WorkstationHistoryEntity workstationHistoryEntity = new WorkstationHistoryEntity();
        workstationHistoryEntity.setId(UUID.randomUUID());
        workstationHistoryEntity.setWorkstationId(workstationEntity.getId());
        workstationHistoryEntity.setState(state);
        workstationHistoryEntity.setFrom(from);
        workstationHistoryEntity.setTo(to);
        WorkstationHistoryEntity workstationHistorySaved = workstationHistoryRepository.insert(workstationHistoryEntity);
        return workstationHistorySaved.getId();
    }

    public Map<String, Integer> getReportIntegration(LocalDate from, LocalDate to) {
        if (!isValidDatesReport(from, to)) {
            throw new IllegalArgumentException("The from date must be before the to date");
        }

        if(hardcodedHistoryRepository.findByFromAndTo(from, to).isPresent()){
            return hardcodedHistoryRepository.findByFromAndTo(from, to).get().getHardcodedValues();
        }

        List<WorkstationHistoryEntity> historyCreated = workstationHistoryRepository.findByFromBetweenAndState(from, to, "CREATED")
                .orElseThrow(() -> new RuntimeException("Error getting workstation history"));
        List<WorkstationHistoryEntity> historyActivated = workstationHistoryRepository.findByFromBetweenAndState(from, to, "ACTIVATED")
                .orElseThrow(() -> new RuntimeException("Error getting workstation history"));

        List<WorkstationHistoryEntity> history = new ArrayList<>(historyCreated);
        history.addAll(historyActivated);
        String unit = getUnitMeasurement(from, to);
        Map<String, Integer> value = reportService.getReport(history, unit, from, to);
        hardcodedHistoryRepository.save(new HardcodedHistoryEntity(
                UUID.randomUUID(),
                from,
                to,
                value
        ));
        return value;
    }

    public Map<String, Integer> getReportDeleted(LocalDate from, LocalDate to) {
        if (!isValidDatesReport(from, to)) {
            throw new IllegalArgumentException("The from date must be before the to date");
        }

        if(hardcodedHistoryRepository.findByFromAndTo(from, to).isPresent()){
            return hardcodedHistoryRepository.findByFromAndTo(from, to).get().getHardcodedValues();
        }

        List<WorkstationHistoryEntity> history = workstationHistoryRepository.findByFromBetweenAndState(from, to, "DELETED")
                .orElseThrow(() -> new RuntimeException("Error getting workstation history"));

        String unit = getUnitMeasurement(from, to);
        Map<String, Integer> value = reportService.getReport(history, unit, from, to);
        hardcodedHistoryRepository.save(new HardcodedHistoryEntity(
                UUID.randomUUID(),
                from,
                to,
                value
        ));
        return value;
    }

    private Boolean isValid(WorkstationRequestDTO workstationRequestDTO) {
        if (isExist(workstationRequestDTO.name())) {
            throw new IllegalArgumentException("Workstation already exists");
        }
        return true;
    }

    public boolean isExist(String name) {
        return workstationRepository.findWorkstationEntityByName(name).isPresent();
    }

    private boolean isValidDatesReport(LocalDate from, LocalDate to) {
        return from.isBefore(to) || from.isEqual(to);
    }

    private String getUnitMeasurement(LocalDate from, LocalDate to) {
        long dayDifference = ChronoUnit.DAYS.between(from, to);
        if (dayDifference <= 7) {
            return "DAY";
        } else if (dayDifference <= 30) {
            return "WEEK";
        } else if (dayDifference <= 365) {
            return "MONTH";
        } else {
            return "YEAR";
        }
    }
}
