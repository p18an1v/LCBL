package com.LeetcodeBeginners.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;
    private String email;
    private String password;
    private String role; // This can be ROLE_USER or ROLE_ADMIN
}

