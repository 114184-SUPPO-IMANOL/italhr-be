package org.apiitalhrbe.controllers;

import jakarta.validation.Valid;
import org.apiitalhrbe.dtos.login.Credential;
import org.apiitalhrbe.dtos.login.LoginResponseDTO;
import org.apiitalhrbe.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid Credential credential) {
        LoginResponseDTO loginResponseDTO = loginService.getByCredential(credential);
        if (Objects.isNull(loginResponseDTO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The credentials are invalid");
        } else {
            return ResponseEntity.ok(loginResponseDTO);
        }
    }
}
