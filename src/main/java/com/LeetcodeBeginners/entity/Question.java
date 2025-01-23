package com.LeetcodeBeginners.entity;

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
    private String questionName;
    private String url;
    private String level;
    private String dataStructure;

    // Reference to the topic
    private ObjectId topicId;
    // Getters and Setters
}
