package org.apiitalhrbe.services;

import jakarta.persistence.EntityNotFoundException;
import org.apiitalhrbe.dtos.request.DepartmentRequestDTO;
import org.apiitalhrbe.dtos.response.DepartmentResponseDTO;
import org.apiitalhrbe.entities.nosql.DepartmentHistoryEntity;
import org.apiitalhrbe.entities.nosql.HardcodedHistoryEntity;
import org.apiitalhrbe.entities.sql.DepartmentEntity;
import org.apiitalhrbe.entities.sql.EmployeeEntity;
import org.apiitalhrbe.repositories.nosql.DepartmentHistoryRepository;
import org.apiitalhrbe.repositories.nosql.HardcodedHistoryRepository;
import org.apiitalhrbe.repositories.sql.DepartmentRepository;
import org.apiitalhrbe.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private ReportService<DepartmentHistoryEntity> reportService;

    @Autowired
    private DepartmentHistoryRepository departmentHistoryRepository;

    @Autowired
    private HardcodedHistoryRepository hardcodedHistoryRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public DepartmentResponseDTO getDepartmentById(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found"));
        return modelMapper.map(departmentEntity, DepartmentResponseDTO.class);
    }

    public DepartmentResponseDTO getDepartmentByEmployeeId(Long employeeId) {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        return departmentEntities.stream()
                .filter(departmentEntity -> departmentEntity.getEmployees().stream().anyMatch(employee -> employee.getId().equals(employeeId)))
                .map(departmentEntity -> modelMapper.map(departmentEntity, DepartmentResponseDTO.class))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        return departmentEntities.stream()
                .map(departmentEntity -> modelMapper.map(departmentEntity, DepartmentResponseDTO.class))
                .toList();
    }

    public List<DepartmentResponseDTO> getAllActiveDepartments() {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        return departmentEntities.stream()
                .filter(DepartmentEntity::getIsActive)
                .map(departmentEntity -> modelMapper.map(departmentEntity, DepartmentResponseDTO.class))
                .toList();
    }

    public Integer getDepartmentVacancies(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.getReferenceById(id);
        return departmentEntity.getCapacity() - departmentEntity.getEmployees().stream().filter(EmployeeEntity::getIsActive).toList().size();
    }

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        if (isValid(departmentRequestDTO)) {
            if (isExist(departmentRequestDTO.name())) {
                throw new IllegalArgumentException("Department already exists");
            }
            DepartmentEntity departmentEntity = modelMapper.map(departmentRequestDTO, DepartmentEntity.class);
            setUpperDepartmentAndChief(departmentRequestDTO, departmentEntity);
            departmentEntity.setEmployees(new ArrayList<>());
            departmentEntity.setCreatedAt(LocalDate.now());
            departmentEntity.setIsActive(true);
            UUID historyId = saveDepartmentHistory(departmentEntity, "CREATED", LocalDate.now(), null);
            if (historyId == null) {
                throw new IllegalArgumentException("Error saving department history");
            }
            DepartmentEntity departmentEntitySaved = departmentRepository.save(departmentEntity);
            return modelMapper.map(departmentEntitySaved, DepartmentResponseDTO.class);
        }
        DepartmentEntity departmentEntity = modelMapper.map(departmentRequestDTO, DepartmentEntity.class);
        departmentRepository.save(departmentEntity);
        return modelMapper.map(departmentEntity, DepartmentResponseDTO.class);
    }

    public DepartmentResponseDTO updateDepartment(DepartmentRequestDTO departmentRequestDTO, Long id) {
        if (isValid(departmentRequestDTO)) {
            DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found"));
            departmentEntity.setName(departmentRequestDTO.name());
            departmentEntity.setCapacity(departmentRequestDTO.capacity());
            setUpperDepartmentAndChief(departmentRequestDTO, departmentEntity);
            departmentEntity.setUpdatedAt(LocalDate.now());
            UUID historyId = saveDepartmentHistory(departmentEntity, "UPDATED", LocalDate.now(), null);
            if (historyId == null) {
                throw new IllegalArgumentException("Error saving department history");
            }
            DepartmentEntity departmentEntitySaved = departmentRepository.save(departmentEntity);
            return modelMapper.map(departmentEntitySaved, DepartmentResponseDTO.class);
        }
        throw new IllegalArgumentException("Invalid department");
    }

    public Long deleteDepartment(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found"));
        if (isValidDelete(departmentEntity)) {
            departmentEntity.setIsActive(false);
            departmentEntity.setUpdatedAt(LocalDate.now());
            UUID historyId = saveDepartmentHistory(departmentEntity, "DELETED", LocalDate.now(), null);
            if (historyId == null) {
                throw new IllegalArgumentException("Error saving department history");
            }
            return departmentRepository.save(departmentEntity).getId();
        }
        throw new IllegalArgumentException("Invalid department");
    }

    public Long activateDepartment(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found"));
        departmentEntity.setIsActive(true);
        departmentEntity.setUpdatedAt(LocalDate.now());
        UUID historyId = saveDepartmentHistory(departmentEntity, "ACTIVATED", LocalDate.now(), null);
        if (historyId == null) {
            throw new IllegalArgumentException("Error saving department history");
        }
        return departmentRepository.save(departmentEntity).getId();
    }

    // TODO: Deuda técnica - Mejorar metodo, devolver un valor
    // TODO: Deuda técnica - aplicar un rollback en caso de error
    public void loadEmployee(Long id, Long employeeId) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found"));
        if (departmentEntity.getEmployees().stream().noneMatch(employee -> employee.getId().equals(employeeId))) {
            EmployeeEntity employeeEntity = new EmployeeEntity();
            employeeEntity.setId(employeeId);
            departmentEntity.getEmployees().add(employeeEntity);
            DepartmentEntity departmentSaved = departmentRepository.save(departmentEntity);
            if (departmentSaved.getEmployees().stream().noneMatch(employee -> employee.getId().equals(employeeId))) {
                throw new EntityNotFoundException("Employee not loaded");
            }
        }
    }

    private void setUpperDepartmentAndChief(DepartmentRequestDTO departmentRequestDTO, DepartmentEntity departmentEntity) {
        if (departmentRequestDTO.upperDepartmentId() != null) {
            DepartmentEntity upperDepartment = new DepartmentEntity();
            upperDepartment.setId(departmentRequestDTO.upperDepartmentId());
            departmentEntity.setUpperDepartment(upperDepartment);
        }
        EmployeeEntity chief = new EmployeeEntity();
        chief.setId(departmentRequestDTO.chiefId());
        departmentEntity.setChief(chief);
    }

    private UUID saveDepartmentHistory(DepartmentEntity departmentEntity, String state, LocalDate from, LocalDate to) {
        DepartmentHistoryEntity departmentHistoryEntity = new DepartmentHistoryEntity();
        departmentHistoryEntity.setId(UUID.randomUUID());
        departmentHistoryEntity.setDepartmentId(departmentEntity.getId());
        departmentHistoryEntity.setState(state);
        departmentHistoryEntity.setFrom(from);
        departmentHistoryEntity.setTo(to);
        return departmentHistoryRepository.save(departmentHistoryEntity).getId();
    }

    public Map<String, Integer> getReportIntegration(LocalDate from, LocalDate to) {
        if (!isValidDatesReport(from, to)) {
            throw new IllegalArgumentException("The from date must be before the to date");
        }

        if(hardcodedHistoryRepository.findByFromAndToAndStatusAndType(from, to, "INTEGRATION", "DEPARTMENT").isPresent()){
            return hardcodedHistoryRepository.findByFromAndToAndStatusAndType(from, to, "INTEGRATION", "DEPARTMENT").get().getHardcodedValues();
        }

        List<DepartmentHistoryEntity> historyCreated = departmentHistoryRepository.findByFromBetweenAndState(from, to, "CREATED")
                .orElseThrow(() -> new RuntimeException("Error getting department history"));
        List<DepartmentHistoryEntity> historyActivated = departmentHistoryRepository.findByFromBetweenAndState(from, to, "ACTIVATED")
                .orElseThrow(() -> new RuntimeException("Error getting department history"));

        List<DepartmentHistoryEntity> history = new ArrayList<>(historyCreated);
        history.addAll(historyActivated);
        String unit = getUnitMeasurement(from, to);
        Map<String, Integer> value = reportService.getReport(history, unit, from, to);
        hardcodedHistoryRepository.save(new HardcodedHistoryEntity(
                UUID.randomUUID(),
                "INTEGRATION",
                "DEPARTMENT",
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

        if(hardcodedHistoryRepository.findByFromAndToAndStatusAndType(from, to, "DELETED", "DEPARTMENT").isPresent()){
            return hardcodedHistoryRepository.findByFromAndToAndStatusAndType(from, to, "DELETED", "DEPARTMENT").get().getHardcodedValues();
        }

        List<DepartmentHistoryEntity> history = departmentHistoryRepository.findByFromBetweenAndState(from, to, "DELETED")
                .orElseThrow(() -> new RuntimeException("Error getting department history"));

        String unit = getUnitMeasurement(from, to);
        Map<String, Integer> value = reportService.getReport(history, unit, from, to);
        hardcodedHistoryRepository.save(new HardcodedHistoryEntity(
                UUID.randomUUID(),
                "DELETED",
                "DEPARTMENT",
                from,
                to,
                value
        ));
        return value;
    }

    private boolean isValid(DepartmentRequestDTO departmentRequestDTO) {
        if (departmentRequestDTO.name() == null || departmentRequestDTO.name().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (departmentRequestDTO.upperDepartmentId() != null) {
            Optional<DepartmentEntity> parentDepartment = departmentRepository.findById(departmentRequestDTO.upperDepartmentId());
            if (parentDepartment.isEmpty()) {
                throw new EntityNotFoundException("Parent department not found");
            }
        }
        if (departmentRequestDTO.capacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        return true;
    }

    private boolean isValidDelete(DepartmentEntity department) {
        if (!department.getEmployees().stream().filter(EmployeeEntity::getIsActive).toList().isEmpty()) {
            throw new IllegalArgumentException("Department has employees");
        }
        if (isSuperior(department.getId())) {
            throw new IllegalArgumentException("Department is superior with others departments");
        }
        return true;
    }

    public boolean isExist(String name) {
        return departmentRepository.findByName(name).isPresent();
    }

    public boolean isAvailable(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found"));
        return departmentEntity.getCapacity() > departmentEntity.getEmployees().stream().filter(EmployeeEntity::getIsActive).toList().size();
    }

    public boolean isActive(Long id) {
        return getDepartmentById(id).isActive();
    }

    public boolean isChief(Long employeeId) {
        return departmentRepository.findAll().stream()
                .anyMatch(departmentEntity -> departmentEntity.getChief().getId().equals(employeeId));
    }

    private boolean isSuperior(Long id) {
        return departmentRepository.findAll().stream()
                .anyMatch(departmentEntity -> departmentEntity.getUpperDepartment() != null
                        && departmentEntity.getUpperDepartment().getId().equals(id)
                        && departmentEntity.getIsActive());
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

