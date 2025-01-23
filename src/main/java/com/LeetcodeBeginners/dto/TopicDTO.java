package com.LeetcodeBeginners.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class TopicDTO {
    private String id;
    @NotEmpty(message = "DataStructure cannot be empty")
    private String dataStructure;
    private List<String> questionIds;

    // Getters and Setters
}
