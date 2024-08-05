package org.apiitalhrbe.services;

import jakarta.persistence.EntityNotFoundException;
import org.apiitalhrbe.dtos.request.EmployeeRequestDTO;
import org.apiitalhrbe.dtos.response.EmployeeDetailResponseDTO;
import org.apiitalhrbe.dtos.response.EmployeeResponseDTO;
import org.apiitalhrbe.entities.nosql.EmployeeHistoryEntity;
import org.apiitalhrbe.entities.sql.EmployeeEntity;
import org.apiitalhrbe.entities.sql.WorkstationEntity;
import org.apiitalhrbe.repositories.nosql.EmployeeHistoryRepository;
import org.apiitalhrbe.repositories.sql.EmployeeRepository;
import org.apiitalhrbe.utils.*;
import org.apiitalhrbe.utils.enums.ContractType;
import org.apiitalhrbe.utils.enums.WorkdayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeHistoryRepository employeeHistoryRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    private ReportService<EmployeeHistoryEntity> reportService;

    private final ModelMapper modelMapper = new ModelMapper();


    public EmployeeResponseDTO getEmployeeById(Long id) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        if (employeeEntity.isEmpty()) {
            throw new EntityNotFoundException("Employee not found");
        }
        return modelMapper.map(employeeEntity.get(), EmployeeResponseDTO.class);
    }

    public EmployeeDetailResponseDTO getEmployeeDetailsById(Long id) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        if (employeeEntity.isEmpty()) {
            throw new EntityNotFoundException("Employee not found");
        }
        EmployeeDetailResponseDTO employeeDetailResponseDTO = modelMapper.map(employeeEntity.get(), EmployeeDetailResponseDTO.class);
        employeeDetailResponseDTO.setDepartmentId(departmentService.getDepartmentByEmployeeId(id).id());
        return employeeDetailResponseDTO;
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        return employeeEntities.stream()
                .map(employeeEntity -> {
                    EmployeeResponseDTO employeeResponseDTO = modelMapper.map(employeeEntity, EmployeeResponseDTO.class);
                    employeeResponseDTO.setDepartmentId(departmentService.getDepartmentByEmployeeId(employeeEntity.getId()).id());
                    return employeeResponseDTO;
                })
                .toList();
    }

    public List<EmployeeResponseDTO> getAllReferents() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        return employeeEntities.stream()
                .filter(EmployeeEntity::getIsReferent)
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeResponseDTO.class))
                .toList();
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        if (isValid(employeeRequestDTO)) {
            if (personService.isExist(employeeRequestDTO.person().documentNumber())) {
                throw new IllegalArgumentException("Person already exists");
            }
            EmployeeEntity employeeEntity = modelMapper.map(employeeRequestDTO, EmployeeEntity.class);
            employeeEntity.setIsActive(true);
            UUID historyId = saveEmployeeHistory(employeeEntity, "CREATED", LocalDate.now(), null);
            if (historyId == null) {
                throw new IllegalArgumentException("Error saving employee history");
            }
            return getEmployeeResponseDTO(employeeRequestDTO, employeeEntity);
        }
        throw new IllegalArgumentException("Invalid employee");
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO) {
        if (isValid(employeeRequestDTO)) {
            EmployeeEntity employeeEntityOptional = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
            EmployeeEntity employeeEntity = modelMapper.map(employeeRequestDTO, EmployeeEntity.class);
            employeeEntity.getPerson().setId(employeeEntityOptional.getPerson().getId());
            employeeEntity.setId(id);
            employeeEntity.setIsActive(employeeEntityOptional.getIsActive());
            UUID historyId = saveEmployeeHistory(employeeEntity, "UPDATED", LocalDate.now(), null);
            if (historyId == null) {
                throw new IllegalArgumentException("Error saving employee history");
            }
            return getEmployeeResponseDTO(employeeRequestDTO, employeeEntity);
        }
        throw new IllegalArgumentException("Invalid employee");
    }

    public Long deleteEmployee(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        if (isValidDelete(employeeEntity)) {
            employeeEntity.setIsActive(false);
            employeeEntity.setUpdatedAt(LocalDate.now());
            UUID historyId = saveEmployeeHistory(employeeEntity, "DELETED", employeeEntity.getUpdatedAt(), null);
            if (historyId == null) {
                throw new IllegalArgumentException("Error saving employee history");
            }
            return employeeRepository.save(employeeEntity).getId();
        }
        throw new IllegalArgumentException("Invalid employee");
    }

    public Long activateEmployee(Long id) {
        EmployeeEntity employeeEntity = employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        employeeEntity.setIsActive(true);
        employeeEntity.setUpdatedAt(LocalDate.now());
        UUID historyId = saveEmployeeHistory(employeeEntity, "ACTIVATED", employeeEntity.getUpdatedAt(), null);
        if (historyId == null) {
            throw new IllegalArgumentException("Error saving employee history");
        }
        return employeeRepository.save(employeeEntity).getId();
    }

    private EmployeeResponseDTO getEmployeeResponseDTO(EmployeeRequestDTO employeeRequestDTO, EmployeeEntity employeeEntity) {
        if (employeeRequestDTO.referentId() != null) {
            Optional<EmployeeEntity> referent = employeeRepository.findById(employeeRequestDTO.referentId());
            if (referent.isEmpty() || !referent.get().getIsReferent()) {
                throw new EntityNotFoundException("Referent not found or dont have permission to referent this employee");
            } else {
                employeeEntity.setReferent(referent.get());
            }
        }
        WorkstationEntity workstationEntity = new WorkstationEntity();
        workstationEntity.setId(employeeRequestDTO.workstationId());
        employeeEntity.setWorkstation(workstationEntity);
        EmployeeEntity employeeEntitySaved = employeeRepository.save(employeeEntity);
        departmentService.loadEmployee(employeeRequestDTO.departmentId(), employeeEntitySaved.getId());
        EmployeeResponseDTO employeeResponseDTO = modelMapper.map(employeeEntitySaved, EmployeeResponseDTO.class);
        employeeResponseDTO.setDepartmentId(employeeRequestDTO.departmentId());
        return employeeResponseDTO;
    }

    // TODO: Deuda técnica - Permitir un rollback en caso de error
    // TODO: Ver uso de fechas
    private UUID saveEmployeeHistory(EmployeeEntity employeeEntity, String state, LocalDate from, LocalDate to) {
        EmployeeHistoryEntity employeeHistoryEntity = new EmployeeHistoryEntity();
        employeeHistoryEntity.setId(UUID.randomUUID());
        employeeHistoryEntity.setEmployeeId(employeeEntity.getId());
        employeeHistoryEntity.setState(state);
        employeeHistoryEntity.setFrom(from);
        employeeHistoryEntity.setTo(to);
        return employeeHistoryRepository.save(employeeHistoryEntity).getId();
    }

    public Map<String, Integer> getReportIntegration(LocalDate from, LocalDate to) {
            if (!isValidDatesReport(from, to)) {
                throw new IllegalArgumentException("The from date must be before the to date");
            }
            List<EmployeeHistoryEntity> employeeHistoryCreated = employeeHistoryRepository.findByFromBetweenAndState(from, to, "CREATED")
                    .orElseThrow(() -> new RuntimeException("Error getting employee history"));
            List<EmployeeHistoryEntity> employeeHistoryActivated = employeeHistoryRepository.findByFromBetweenAndState(from, to, "ACTIVATED")
                    .orElseThrow(() -> new RuntimeException("Error getting employee history"));
            if (employeeHistoryCreated.isEmpty() && employeeHistoryActivated.isEmpty()) {
                throw new RuntimeException("Employee history not found in the range of dates: " + from + " - " + to);
            }
            List<EmployeeHistoryEntity> employeeHistory = new ArrayList<>(employeeHistoryCreated);
            employeeHistory.addAll(employeeHistoryActivated);
            String unit = getUnitMeasurement(from, to);
            return reportService.getReport(employeeHistory, unit, from, to);
    }

    public Map<String, Integer> getReportDeleted(LocalDate from, LocalDate to) {
        if (!isValidDatesReport(from, to)) {
            throw new IllegalArgumentException("The from date must be before the to date");
        }
        List<EmployeeHistoryEntity> employeeHistoryDeleted = employeeHistoryRepository.findByFromBetweenAndState(from, to, "DELETED")
                .orElseThrow(() -> new RuntimeException("Error getting employee history"));
        if (employeeHistoryDeleted.isEmpty()) {
            throw new RuntimeException("Employee history not found in the range of dates: " + from + " - " + to);
        }
        String unit = getUnitMeasurement(from, to);
        return reportService.getReport(employeeHistoryDeleted, unit, from, to);
    }

    private boolean isValid(EmployeeRequestDTO employee) {
        if (!personService.isValid(employee.person())) {
            return false;
        }
        if (workstationService.getWorkstationById(employee.workstationId()) == null) {
            throw new IllegalArgumentException("Workstation not found");
        }
        if (departmentService.getDepartmentById(employee.departmentId()) == null) {
            throw new IllegalArgumentException("Department not found");
        }
        if (!departmentService.isActive(employee.departmentId())) {
            throw new IllegalArgumentException("Department is not active");
        }
        if (!departmentService.isAvailable(employee.departmentId())) {
            throw new IllegalArgumentException("Department is not available");
        }
        if (!isValidWorkdayType(employee.workdayType())) {
            throw new IllegalArgumentException("Invalid workday type");
        }
        if (!isValidContractType(employee.contractType())) {
            throw new IllegalArgumentException("Invalid contract type");
        }
        if (employee.contractType().equals(ContractType.TEMPORARY.toString())
                && !isValidContractDates(employee.startDate(), employee.contractFrom(), employee.contractTo())) {
            return false;
        }
        if(employee.otherRemarks().requestReceived().isAfter(employee.startDate()) &&
                employee.authorization().requestReceived().isAfter(employee.startDate())){
            throw new IllegalArgumentException("Request received date must be before start date");
        }
        if (!employee.authorization().isAuthorized()) {
            throw new IllegalArgumentException("Employee is not authorized");
        }
        return true;
    }

    private boolean isValidWorkdayType(String workdayType) {
        try {
            WorkdayType.valueOf(workdayType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidContractType(String contractType) {
        try {
            ContractType.valueOf(contractType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidContractDates(LocalDate starDate, LocalDate contractFrom, LocalDate contractTo) {
        if (contractFrom.isAfter(contractTo)) {
            throw new IllegalArgumentException("Contract from date must be before contract to date");
        }
        if (contractFrom.isBefore(starDate)) {
            throw new IllegalArgumentException("Contract from date must be after start date");
        }
        return true;
    }

    private boolean isValidDelete(EmployeeEntity employeeEntity) {
        if (employeeEntity.getIsReferent()){
            throw new IllegalArgumentException("Referent cannot be deleted");
        }
        if (departmentService.isChief(employeeEntity.getId())) {
            throw new IllegalArgumentException("Chief cannot be deleted");
        }
        return true;
    }

    // TODO: Código duplicado en otros servicios
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
