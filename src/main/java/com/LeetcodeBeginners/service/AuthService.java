package com.LeetcodeBeginners.service;

import com.LeetcodeBeginners.dto.LoginDTO;
import com.LeetcodeBeginners.dto.PasswordResetToken;
import com.LeetcodeBeginners.dto.UserRegistrationDTO;
import com.LeetcodeBeginners.dto.UserResponseDTO;
import com.LeetcodeBeginners.entity.User;
import com.LeetcodeBeginners.entity.UserProgress;
import com.LeetcodeBeginners.repository.PasswordResetTokenRepository;
import com.LeetcodeBeginners.repository.UserProgressRepository;
import com.LeetcodeBeginners.repository.UserRepository;
import com.LeetcodeBeginners.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final UserProgressRepository userProgressRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, PasswordResetTokenRepository tokenRepository, EmailService emailService, ModelMapper modelMapper, UserProgressRepository userProgressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.userProgressRepository = userProgressRepository;
    }

    public String registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + registrationDTO.getEmail());
        }

        //save user
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole() != null ? registrationDTO.getRole() : "ROLE_USER");//"ROLE_USER"
        userRepository.save(user);

        // Create an empty UserProgress document for the user
        UserProgress userProgress = new UserProgress();
        userProgress.setUserId(user.getId());
        userProgress.setUserEmail(user.getEmail());
        userProgress.setCompletedQuestions(new ArrayList<>()); // Empty list
        userProgress.setTotalSolved(0); // No questions solved initially
        userProgressRepository.save(userProgress);
        return "User registered successfully with role: " + user.getRole();
    }

    public String authenticateUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }


    public String generatePasswordResetToken(String email) {
        System.out.println("Searching for user with email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Remove any existing tokens for the user
        tokenRepository.deleteByUserId(user.getId().toString());

        System.out.println("User found: " + user);

        // Generate the token (or perform other actions as necessary)
        String token = UUID.randomUUID().toString();

        // Create and save the reset token
        PasswordResetToken resetToken = new PasswordResetToken(null, token, user.getId(), LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);


        // Send reset email
//        String resetLink = "http://yourfrontenddomain.com/reset-password?token=" + token;
//        emailService.sendEmail(user.getEmail(), "Password Reset Request",
//                "Click the link to reset your password: " + resetLink);

        // Send email with reset link
        emailService.sendEmail(user.getEmail(), "Password Reset", "Your reset token is: " + token);
        return token;
    }

    // Add the method to fetch user details by email
    public UserResponseDTO getUserDetailsByEmail(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Map the user entity to UserResponseDTO
        UserResponseDTO userResponse = modelMapper.map(user, UserResponseDTO.class);

        return userResponse;
    }



    // Reset Password with the provided token
    public boolean resetPassword(String token, String newPassword) {
        // Fetch the token from the repository
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        // Check if the token is expired
        if (resetToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken); // Delete expired token
            return false;
        }

        // Fetch the user using the ObjectId
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the token after successful password reset
        tokenRepository.delete(resetToken);
        return true;
    }


    public void deleteUser(String email, String password) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Validate the password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Delete user progress associated with the user
        userProgressRepository.deleteById(user.getId());

        // Delete the user
        userRepository.delete(user);
    }



}
