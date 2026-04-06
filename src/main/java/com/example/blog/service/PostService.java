package com.example.blog.service;


import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post savePost(Post post){
        return postRepository.save(post);
    }

    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }

    public List<Post> findAllPosts(){
        return postRepository.findAll();
    }

    public List<Post> findByUserId(Long userId){
        return postRepository.findByUserId(userId);
    }

    public List<Post> findPublishedPost(){
        return postRepository.findByStatus("PUBLISHED");
    }

    public void deletePost(Long id){
        postRepository.deleteById(id);
    }




}
