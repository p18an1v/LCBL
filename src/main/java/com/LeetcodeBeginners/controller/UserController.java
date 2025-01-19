package com.LeetcodeBeginners.controller;


import com.LeetcodeBeginners.dto.UserResponseDTO;
import com.LeetcodeBeginners.service.UserService;
import com.LeetcodeBeginners.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Endpoint to get user details after login
    @GetMapping("/details")
    public ResponseEntity<UserResponseDTO> getUserDetails(HttpServletRequest request) {
        // Extract JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization Header: " + authHeader);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        // Validate and extract email from the token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String email = jwtUtil.extractEmail(token);

        // Fetch user details from the database
        UserResponseDTO userResponse = userService.getUserDetailsByEmail(email);

        return ResponseEntity.ok(userResponse);
    }


}
