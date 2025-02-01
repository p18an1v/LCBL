package com.LeetcodeBeginners.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "topics")
public class Topic {
    @Id
    private ObjectId id;
    @NotBlank(message = "Email is required")
    private String dataStructure;
    // List of question IDs (if you want to store references)
    private List<ObjectId> questionIds = new ArrayList<>();

    // Getters and Setters
}

