package com.example.blog.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String id;
    private Long postId;
    private String body;
    private String authorName;
    private String parentId;
    private LocalDateTime createdAt;
}