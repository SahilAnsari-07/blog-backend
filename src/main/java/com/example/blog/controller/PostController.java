package com.example.blog.controller;


import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.service.PostService;
import com.example.blog.service.S3Service;
import com.example.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest, Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .status(postRequest.getStatus())
                .imageUrl(postRequest.getImageUrl())
                .user(user)
                .build();

        Post savedPost = postService.savePost(post);

        PostResponse postResponse = PostResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .status(savedPost.getStatus())
                .imageUrl(savedPost.getImageUrl())
                .authorName(savedPost.getUser().getName())
                .createdAt(savedPost.getCreatedAt())
                .updatedAt(savedPost.getUpdatedAt())
                .build();

        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.findAllPosts();

        List<PostResponse> postResponses = posts.stream()
                .map(post -> PostResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .status(post.getStatus())
                        .imageUrl(post.getImageUrl())
                        .authorName(post.getUser().getName())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .imageUrl(post.getImageUrl())
                .authorName(post.getUser().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return ResponseEntity.ok(postResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest, Authentication authentication) {
        Post post = postService.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String email = authentication.getName();
        if (!post.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("You can only update your own posts!");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setStatus(postRequest.getStatus());
        post.setImageUrl(postRequest.getImageUrl());

        Post updatedPost = postService.savePost(post);

        PostResponse postResponse = PostResponse.builder()
                .id(updatedPost.getId())
                .title(updatedPost.getTitle())
                .content(updatedPost.getContent())
                .status(updatedPost.getStatus())
                .imageUrl(updatedPost.getImageUrl())
                .authorName(updatedPost.getUser().getName())
                .createdAt(updatedPost.getCreatedAt())
                .updatedAt(updatedPost.getUpdatedAt())
                .build();

        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        Post post = null;
        try{
            post = postService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
        }catch (Exception e){

                return ResponseEntity.badRequest().body("Post Does not Exist");

        }





        String email = authentication.getName();
        if (!post.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("You can only delete your own posts!");
        }
        if (post.getImageUrl() != null){
            s3Service.deleteFile(post.getImageUrl());
        }

        postService.deletePost(id);

        return ResponseEntity.ok("Post deleted successfully!");
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile file){
        try {
            String imgUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok(imgUrl);
        }catch (IOException e){
            return ResponseEntity.status(500).body("Image upload Failed "+ e.getMessage());

        }
    }



}
