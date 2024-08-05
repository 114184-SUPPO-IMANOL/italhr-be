package org.apiitalhrbe.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.apiitalhrbe.dtos.request.DepartmentRequestDTO;
import org.apiitalhrbe.dtos.response.DepartmentResponseDTO;
import org.apiitalhrbe.dtos.response.EmployeeResponseDTO;
import org.apiitalhrbe.services.DepartmentService;
import org.apiitalhrbe.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeController employeeController;

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/actives")
    public ResponseEntity<List<DepartmentResponseDTO>> getAllActiveDepartments(){
        return ResponseEntity.ok(departmentService.getAllActiveDepartments());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsDepartmentById(@RequestParam String name){
        return ResponseEntity.ok(departmentService.isExist(name));
    }

    @GetMapping("/{id}/vacancies")
    public ResponseEntity<Integer> getDepartmentCapacity(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.getDepartmentVacancies(id));
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@RequestBody DepartmentRequestDTO departmentRequestDTO){
        if (departmentRequestDTO.chiefId() != null) {
            validateChief(departmentRequestDTO);
            return ResponseEntity.ok(departmentService.createDepartment(departmentRequestDTO));
        }
        throw new IllegalArgumentException("Department need a chief");
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@RequestBody DepartmentRequestDTO departmentRequestDTO, @PathVariable Long id){
        if (departmentRequestDTO.chiefId() != null) {
            validateChief(departmentRequestDTO);
            return ResponseEntity.ok(departmentService.updateDepartment(departmentRequestDTO, id));
        }
        throw new IllegalArgumentException("Department need a chief");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteDepartment(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.deleteDepartment(id));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Long> activateDepartment(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.activateDepartment(id));
    }

    @GetMapping("/reports/{status}")
    public ResponseEntity<Map<String, Integer>> getReports(@RequestParam LocalDate from, @RequestParam LocalDate to, @PathVariable String status){
        if (status.equals("integrations")) {
            return ResponseEntity.ok(departmentService.getReportIntegration(from, to));
        }
        if (status.equals("deleted")) {
            return ResponseEntity.ok(departmentService.getReportDeleted(from, to));
        }
        throw new IllegalArgumentException("Invalid status");
    }

    private void validateChief(@RequestBody DepartmentRequestDTO departmentRequestDTO) {
        ResponseEntity<EmployeeResponseDTO> chiefResponse = employeeController.getEmployeeById(departmentRequestDTO.chiefId());
        if (chiefResponse.getStatusCode().value() == 500) {
            throw new EntityNotFoundException("Error getting chief");
        }
        if (chiefResponse.getStatusCode().value() == 404) {
            throw new EntityNotFoundException("Chief not found");
        }
        if (chiefResponse.getStatusCode().value() == 200) {
            EmployeeResponseDTO chief = chiefResponse.getBody();
            if (chief != null && !chief.getIsReferent()) {
                throw new EntityNotFoundException("Chief not found or dont have permission to be chief of this department");
            }
        }
    }
}
