package com.example.blog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Field("post_id")
    private Long postId;

    @Field("user_id")
    private Long userId;

    @Field("username")
    private String username;

    private String body;

    private String parentId;

    @CreatedDate
    private LocalDateTime createdAt;
}