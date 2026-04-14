package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotBlank(message = "Comment cannot be empty")
    private String body;

    private Long parentId;
}