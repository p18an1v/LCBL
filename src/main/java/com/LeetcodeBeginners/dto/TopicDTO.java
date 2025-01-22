package com.LeetcodeBeginners.dto;

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
    private String dataStructure;
    private List<QuestionDTO> questionsList;

    // Getters and Setters
}
