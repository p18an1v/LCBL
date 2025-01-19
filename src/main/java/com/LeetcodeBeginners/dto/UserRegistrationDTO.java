package com.LeetcodeBeginners.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class UserRegistrationDTO {

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        @Indexed(unique = true)
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private String role; // Optional, defaults to ROLE_USER
}

