package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long postId;

    @NotBlank(message = "Comment cannot be empty")
    private String body;
    private String parentId;
}