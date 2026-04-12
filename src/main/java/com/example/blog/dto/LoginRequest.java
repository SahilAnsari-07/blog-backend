package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Enter Email")
    private String email;
    @NotBlank(message = "Enter password")
    private String password;
}