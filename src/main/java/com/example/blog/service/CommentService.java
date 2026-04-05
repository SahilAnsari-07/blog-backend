package com.example.blog.service;


import com.example.blog.model.Comment;
import com.example.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    public Comment saveComment(Comment comment){
        return commentRepository.save(comment);
    }

    public List<Comment> findByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> findByReplies(String parentId){
        return commentRepository.findByParentId(parentId);
    }

    public void deleteComment(String id){
        commentRepository.deleteById(id);
    }



}
