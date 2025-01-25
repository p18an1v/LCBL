package com.LeetcodeBeginners.controller;

import com.LeetcodeBeginners.dto.*;
import com.LeetcodeBeginners.service.AdminService;
import com.LeetcodeBeginners.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AdminService adminService;
    private final ModelMapper modelMapper;

    public AuthController(AuthService authService, AdminService adminService, ModelMapper modelMapper) {
        this.authService = authService;
        this.adminService = adminService;
        this.modelMapper = modelMapper;
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

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // Call the service method to delete the user
        authService.deleteUser(email, password);

        return ResponseEntity.ok("User and their progress deleted successfully");
    }

    /**
     * Get all topics
     */
    @GetMapping("/topics/getAll")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> topics = adminService.getAllTopics();
        List<TopicDTO> dtos = topics.stream()
                .map(topic -> modelMapper.map(topic, TopicDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get all patterns
     */
    @GetMapping("/patterns/getAll")
    public ResponseEntity<List<PatternDTO>> getAllPatterns() {
        List<PatternDTO> patterns = adminService.getAllPatterns();
        return ResponseEntity.ok(patterns);
    }
}
