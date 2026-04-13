package com.example.blog.controller;


import com.example.blog.dto.CommentRequest;
import com.example.blog.dto.CommentResponse;
import com.example.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;



    @PostMapping
    public ResponseEntity<?> createComment(@Valid  @RequestBody CommentRequest commentRequest, Authentication authentication) {
        String email = authentication.getName();
        CommentResponse savedComment = commentService.saveComment(commentRequest, email);
        return ResponseEntity.ok(savedComment);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id, Authentication authentication) {
        commentService.deleteComment(id, authentication.getName());
        return ResponseEntity.ok("Comment deleted successfully!");
    }

}
