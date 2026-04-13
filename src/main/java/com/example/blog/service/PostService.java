package com.example.blog.service;


import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.exception.UnauthorizedActionException;
import com.example.blog.mapper.PostMapper;
import com.example.blog.model.Category;
import com.example.blog.model.Post;
import com.example.blog.model.PostStatus;
import com.example.blog.model.User;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final S3Service s3Service;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;


    //create post
    public PostResponse savePost(PostRequest postRequest, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + postRequest.getCategoryId()));
        Post post = postMapper.toEntity(postRequest);
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    //get post by id
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not Found for post Id" + id));
        return postMapper.toResponse(post);
    }

    //get all post
    public List<PostResponse> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::toResponse).toList();
    }

    //get post by user id
    public List<PostResponse> findByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(postMapper::toResponse).toList();
    }

    //update post
    public PostResponse updatePost(Long id, PostRequest postRequest, String email) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post Not Found for the post id " + id));
        if (!post.getUser().getEmail().equals(email)) {
            throw new UnauthorizedActionException("You can only modify your own posts!");
        }
        Category category = categoryRepository.findById(postRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + postRequest.getCategoryId()));
        post.setCategory(category);

        if (postRequest.getStatus() == null) {
            post.setStatus(PostStatus.PUBLISHED);
        }

        postMapper.updateEntityFromDto(postRequest, post);
        post.setStatus(PostStatus.valueOf(postRequest.getStatus().name()));

        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);


    }

    public List<PostResponse> findPublishedPost() {
        List<Post> publishedPost = postRepository.findByStatus(PostStatus.PUBLISHED);
        return publishedPost.stream().map(postMapper::toResponse).toList();
    }
    @Transactional
    public void deletePost(Long id, String email) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post Not Found for the post id " + id));
        if (!post.getUser().getEmail().equals(email)) {
            throw new UnauthorizedActionException("You can only modify your own posts!");
        }
        if (post.getImageUrl() != null) {
            s3Service.deleteFile(post.getImageUrl());
        }
        commentRepository.deleteByPostId(post.getId());
        postRepository.deleteById(id);
    }


}
