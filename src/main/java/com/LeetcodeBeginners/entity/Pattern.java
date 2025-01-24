package com.LeetcodeBeginners.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "patterns")
public class Pattern {
    @Id
    private ObjectId id;
    private String pattern;
    private List<String> questionIds = new ArrayList<>();
}
