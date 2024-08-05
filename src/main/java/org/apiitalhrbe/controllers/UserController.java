package org.apiitalhrbe.controllers;

import org.apiitalhrbe.dtos.request.UserRequestDTO;
import org.apiitalhrbe.dtos.response.TokenResponseDTO;
import org.apiitalhrbe.dtos.response.UserResponseDTO;
import org.apiitalhrbe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(@RequestParam(required = false) String email) {
        if (email != null) {
            return ResponseEntity.ok(List.of(userService.getByEmail(email)));
        }
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @PostMapping("/generate-token")
    public ResponseEntity<TokenResponseDTO> generateToken(@RequestBody String email) {
        return ResponseEntity.ok(userService.generateTokenByEmail(email));
    }

    @PostMapping("/access-token/{id}")
    public ResponseEntity<Boolean> getAccessToken(@PathVariable Long id, @RequestBody String token) {
        return ResponseEntity.ok(userService.validateToken(id, token));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody String password) {
        return ResponseEntity.ok(userService.changePassword(id, password));
    }
}
