package com.example.blog.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor

public class JwtAuthResponse {
    private String token;
    private String userName;
    private String role;

    public JwtAuthResponse(String token, String userName, String role){
        this.token = token;
        this.role= role;
        this.userName = userName;
    }
}
