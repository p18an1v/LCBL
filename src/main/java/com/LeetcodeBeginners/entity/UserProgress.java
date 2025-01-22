package com.LeetcodeBeginners.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user_progress")
public class UserProgress {
    @Id
    private ObjectId userId;
    private String userEmail;
    private List<String> completedQuestions; // List of question IDs
    private int totalSolved;

    // Getters and Setters
}

