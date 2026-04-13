package com.example.blog.dto;

import com.example.blog.model.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private PostStatus status;

    private String imageUrl;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}