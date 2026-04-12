package com.example.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String name;
    @NotBlank(message = "User name is required")
    private String userName;
    @Email(message = "Please enter a valid Email")
    private String email;
    private String password;
}
