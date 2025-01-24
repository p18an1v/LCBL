package com.LeetcodeBeginners.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatternDTO {
    private String id;
    private String pattern;
    private List<String> questionIds;
}
