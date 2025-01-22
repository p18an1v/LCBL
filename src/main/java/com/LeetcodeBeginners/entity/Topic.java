package com.LeetcodeBeginners.entity;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "topics")
public class Topic {
    @Id
    private ObjectId id;
    private String dataStructure;
    private List<Question> questionsList;

    // Getters and Setters
}

