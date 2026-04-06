package com.example.blog.controller;


import com.example.blog.dto.CommentRequest;
import com.example.blog.dto.CommentResponse;
import com.example.blog.model.Comment;
import com.example.blog.model.User;
import com.example.blog.service.CommentService;
import com.example.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.CommentEvent;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest, Authentication authentication){
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .userId(user.getId())
                .body(commentRequest.getBody())
                .parentId(commentRequest.getParentId())
                .build();

        Comment savedComment = commentService.saveComment(comment);

        CommentResponse response = CommentResponse.builder()
                .id(savedComment.getId())
                .postId(savedComment.getPostId())
                .body(savedComment.getBody())
                .authorName(user.getName())
                .parentId(savedComment.getParentId())
                .createdAt(savedComment.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);

        List<CommentResponse> responses = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .postId(comment.getPostId())
                        .body(comment.getBody())
                        .parentId(comment.getParentId())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id, Authentication authentication) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully!");
    }
}
