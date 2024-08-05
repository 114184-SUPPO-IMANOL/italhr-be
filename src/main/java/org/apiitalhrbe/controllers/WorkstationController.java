package org.apiitalhrbe.controllers;

import org.apiitalhrbe.dtos.request.WorkstationRequestDTO;
import org.apiitalhrbe.dtos.response.EmployeeResponseDTO;
import org.apiitalhrbe.dtos.response.WorkstationDetailResponseDTO;
import org.apiitalhrbe.dtos.response.WorkstationResponseDTO;
import org.apiitalhrbe.services.WorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workstations")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkstationController {

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    private EmployeeController employeeController;

    @GetMapping("/{id}")
    public ResponseEntity<WorkstationResponseDTO> getWorkstationById(@PathVariable Long id){
        return ResponseEntity.ok(workstationService.getWorkstationById(id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<WorkstationDetailResponseDTO> getWorkstationDetailById(@PathVariable Long id){
        return ResponseEntity.ok(workstationService.getWorkstationDetailById(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkstationResponseDTO>> getAllWorkstations(){
        return ResponseEntity.ok(workstationService.getAllWorkstations());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsWorkstationById(@RequestParam String name){
        return ResponseEntity.ok(workstationService.isExist(name));
    }

    @GetMapping("/actives")
    public ResponseEntity<List<WorkstationResponseDTO>> getAllActiveWorkstations(){
        return ResponseEntity.ok(workstationService.getAllActiveWorkstations());
    }

    @PostMapping
    public ResponseEntity<WorkstationResponseDTO> saveWorkstation(@RequestBody WorkstationRequestDTO workstationRequestDTO){
        return ResponseEntity.ok(workstationService.createWorkstation(workstationRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkstationResponseDTO> updateWorkstation(@PathVariable Long id, @RequestBody WorkstationRequestDTO workstationRequestDTO){
        return ResponseEntity.ok(workstationService.updateWorkstation(workstationRequestDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteWorkstation(@PathVariable Long id){
        if (isValidDelete(id)) {
            return ResponseEntity.ok(workstationService.deleteWorkstation(id));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Long> activateWorkstation(@PathVariable Long id){
        return ResponseEntity.ok(workstationService.activateWorkstation(id));
    }

    @GetMapping("/reports/{status}")
    public ResponseEntity<Map<String, Integer>> getReports(@RequestParam LocalDate from, @RequestParam LocalDate to, @PathVariable String status){
        if (status.equals("integrations")) {
            return ResponseEntity.ok(workstationService.getReportIntegration(from, to));
        }
        if (status.equals("deleted")) {
            return ResponseEntity.ok(workstationService.getReportDeleted(from, to));
        }
        throw new IllegalArgumentException("Invalid status");
    }

    private boolean isValidDelete(Long id){
        List<EmployeeResponseDTO> employees = employeeController.getAllEmployees().getBody();
        if (employees != null && employees.stream().anyMatch(employee -> employee.getWorkstationId() != null
                && employee.getWorkstationId().equals(id)
                && employee.getIsActive())) {
            throw new IllegalArgumentException("Workstation is in use");
        }
        return true;
    }
}
