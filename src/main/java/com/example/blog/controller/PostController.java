package com.example.blog.controller;


import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.service.PostService;
import com.example.blog.service.S3Service;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest, Authentication authentication) {
        String email = authentication.getName();
        PostResponse postResponse = postService.savePost(postRequest, email);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<PostResponse> posts = postService.findAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        PostResponse post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, Authentication authentication) {


        String email = authentication.getName();
        PostResponse post = postService.updatePost(id, postRequest, email);
        return ResponseEntity.ok(post);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        postService.deletePost(id, email);
        return ResponseEntity.ok("Post deleted successfully!");

    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imgUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok(imgUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload Failed " + e.getMessage());

        }
    }


}
