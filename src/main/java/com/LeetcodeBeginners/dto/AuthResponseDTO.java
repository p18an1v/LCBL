package com.LeetcodeBeginners.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuthResponseDTO {
    private String token;
    private String message;
}
