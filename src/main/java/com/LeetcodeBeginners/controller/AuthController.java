package com.LeetcodeBeginners.controller;

import com.LeetcodeBeginners.dto.AuthResponseDTO;
import com.LeetcodeBeginners.dto.LoginDTO;
import com.LeetcodeBeginners.dto.PasswordResetRequest;
import com.LeetcodeBeginners.dto.UserRegistrationDTO;
import com.LeetcodeBeginners.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


    // Step 1: Request Password Reset
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String token = authService.generatePasswordResetToken(email);
        // Simulate sending email (replace this with actual email service)
        System.out.println("Password reset token for " + email + ": " + token);
        return ResponseEntity.ok("Password reset token sent to email!" + token);
    }

    // Step 2: Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        boolean success = authService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok("Password updated successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token!");
    }
}
