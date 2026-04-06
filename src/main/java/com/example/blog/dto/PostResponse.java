package com.example.blog.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String status;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
}