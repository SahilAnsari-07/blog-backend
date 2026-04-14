package com.example.blog.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private String body;
    private String username;
    private Long parentId;
    private LocalDateTime createdAt;
}