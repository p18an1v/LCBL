package com.LeetcodeBeginners.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class QuestionDTO {
    private String questionId;

    @NotBlank(message = "Question name is required")
    private String questionName;

    @NotBlank(message = "URL is required")
    private String url;

    @NotBlank(message = "Level is required")
    private String level;

    @NotBlank(message = "Data structure is required")
    private String dataStructure;

    // Getters and Setters
}

