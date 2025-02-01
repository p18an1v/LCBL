package com.LeetcodeBeginners.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "questions")
public class Question {
    @Id
    private ObjectId questionId;
    @NotBlank(message = "Question name is required")
    private String questionName;
    @NotBlank(message = "Question url is required")
    private String url;
    @NotBlank(message = "Question level is required")
    private String level;
    @NotBlank(message = "Question data structure is required")
    private String dataStructure;

    // Reference to the topic
    private ObjectId topicId;
    // Getters and Setters
}
