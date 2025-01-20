package com.LeetcodeBeginners.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "passwordResetToken")
public class PasswordResetToken {
    @Id
    private ObjectId id;

    private String token;
    private ObjectId userId; // Matches User's ID type
    private LocalDateTime expirationTime;


    // Constructors, getters, setters
}

