package org.apiitalhrbe.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apiitalhrbe.dtos.common.ErrorApi;
import org.apiitalhrbe.dtos.response.PersonResponseDTO;
import org.apiitalhrbe.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Operation(
            summary = "Get Person by document number",
            description = "Retrieve an person by their document number)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = PersonResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Person not found",
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
    @GetMapping
    public ResponseEntity<PersonResponseDTO> gePersonByDocumentNumber(@RequestParam String documentNumber){
        return ResponseEntity.ok(personService.getPersonByDocumentNumber(documentNumber));
    }
}
