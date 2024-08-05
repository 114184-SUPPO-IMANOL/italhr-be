package org.apiitalhrbe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apiitalhrbe.dtos.common.ErrorApi;
import org.apiitalhrbe.dtos.request.EmployeeRequestDTO;
import org.apiitalhrbe.dtos.response.EmployeeDetailResponseDTO;
import org.apiitalhrbe.dtos.response.EmployeeResponseDTO;
import org.apiitalhrbe.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Operation(
            summary = "Get Employee by ID",
            description = "Retrieve an employee by their unique identifier (ID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<EmployeeDetailResponseDTO> getEmployeeDetailsById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getEmployeeDetailsById(id));
    }

    @GetMapping("/referents")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllReferents(){
        return ResponseEntity.ok(employeeService.getAllReferents());
    }

    @Operation(
            summary = "Create Employee",
            description = "Create a new employee."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = EmployeeResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> saveEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO){
        return ResponseEntity.ok(employeeService.createEmployee(employeeRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDTO employeeRequestDTO){
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteEmployee(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Long> activateEmployee(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.activateEmployee(id));
    }

    @GetMapping("/reports/{status}")
    public ResponseEntity<Map<String, Integer>> getReports(@RequestParam LocalDate from, @RequestParam LocalDate to, @PathVariable String status){
        if (status.equals("integrations")) {
            return ResponseEntity.ok(employeeService.getReportIntegration(from, to));
        }
        if (status.equals("deleted")) {
            return ResponseEntity.ok(employeeService.getReportDeleted(from, to));
        }
        throw new IllegalArgumentException("Invalid status");
    }

    @GetMapping("/reports/hardcoded/{status}")
    public ResponseEntity<Map<String, Integer>> getReportHardcodedData(@PathVariable String status, @RequestParam String unit){
        return ResponseEntity.ok(getHardcodedData(unit));
    }

    public Map<String, Integer> getHardcodedData(String unit) {
        Map<String, Integer> data = new HashMap<>();

        if (unit.equals("MONTH")) {
            data.put("Enero", 10);
            data.put("Febrero", 20);
            data.put("Marzo", 32);
            data.put("Abril", 15);
            data.put("Mayo", 25);
            data.put("Junio", 30);
            data.put("Julio", 10);
            data.put("Agosto", 20);
            data.put("Septiembre", 32);
            data.put("Octubre", 15);
            data.put("Noviembre", 25);
            data.put("Diciembre", 30);
        } else if (unit.equals("DAY")) {
            data.put("Lunes", 10);
            data.put("Martes", 20);
            data.put("Miercoles", 32);
            data.put("Jueves", 15);
            data.put("Viernes", 25);
            data.put("Sabado", 30);
            data.put("Domingo", 10);
        } else if (unit.equals("WEEK")) {
            data.put("Semana 1", 10);
            data.put("Semana 2", 20);
            data.put("Semana 3", 32);
            data.put("Semana 4", 15);
            data.put("Semana 5", 25);
        } else {
            data.put("2012", 10);
            data.put("2013", 20);
            data.put("2014", 32);
            data.put("2015", 15);
            data.put("2016", 25);
            data.put("2017", 30);
            data.put("2018", 10);
            data.put("2019", 20);
            data.put("2020", 32);
            data.put("2021", 15);
            data.put("2022", 25);
            data.put("2023", 30);
        }

        return data;
    }

}
