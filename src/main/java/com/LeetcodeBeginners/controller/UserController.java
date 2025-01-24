package com.LeetcodeBeginners.controller;


import com.LeetcodeBeginners.dto.UserProgressDTO;
import com.LeetcodeBeginners.dto.UserResponseDTO;
import com.LeetcodeBeginners.service.AuthService;
import com.LeetcodeBeginners.service.UserService;
import com.LeetcodeBeginners.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(UserService userService, AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // Endpoint to get user details after login
    @GetMapping("/details")
    public ResponseEntity<UserResponseDTO> getUserDetails(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = jwtUtil.extractEmail(token);
        UserResponseDTO userResponse = authService.getUserDetailsByEmail(email);

        return ResponseEntity.ok(userResponse);
    }

    // Get user progress
    @GetMapping("/progress/{userId}")
    public ResponseEntity<UserProgressDTO> getUserProgress(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProgress(userId));
    }

    // Track a solved question
    @PostMapping("/progress/{userId}/toggle")
    public ResponseEntity<UserProgressDTO> toggleQuestion(
            @PathVariable String userId,
            @RequestParam String questionId,
            @RequestParam boolean completed) {

        if (completed) {
            return ResponseEntity.ok(userService.trackQuestion(userId, questionId));
        } else {
            return ResponseEntity.ok(userService.untrackQuestion(userId, questionId));
        }
    }

}
