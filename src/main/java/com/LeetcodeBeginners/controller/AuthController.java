package com.LeetcodeBeginners.controller;

import com.LeetcodeBeginners.dto.AuthResponseDTO;
import com.LeetcodeBeginners.dto.LoginDTO;
import com.LeetcodeBeginners.dto.UserRegistrationDTO;
import com.LeetcodeBeginners.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO registrationDTO) {
        String response = authService.registerUser(registrationDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        // Authenticate user and generate JWT token
        String token = authService.authenticateUser(loginDTO);

        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setToken(token);
        responseDTO.setMessage("Login successful");

        return ResponseEntity.ok(responseDTO);
    }
}
