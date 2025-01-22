package com.LeetcodeBeginners.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserProgressDTO {
    private String userId;
    private String userEmail;
    private List<String> completedQuestions;
    private int totalSolved;

    // Getters and Setters
}

