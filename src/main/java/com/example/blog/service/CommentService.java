package com.example.blog.service;


import com.example.blog.dto.CommentRequest;
import com.example.blog.dto.CommentResponse;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.exception.UnauthorizedActionException;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.model.Comment;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentMapper commentMapper;


    public CommentResponse saveComment(CommentRequest commentRequest, String email){

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Comment comment = commentMapper.toEntity(commentRequest);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUserName());

        Comment savedComment =  commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    public List<CommentResponse> findByPostId(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentResponse> commentResponses = comments.stream().map(commentMapper::toResponse).toList();
        return commentResponses;
    }

    public List<CommentResponse> findByReplies(String parentId){
        List<Comment> comments = commentRepository.findByParentId(parentId);
        return comments.stream().map(commentMapper::toResponse).toList();
    }

    public void deleteComment(String commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        if (!comment.getUserId().equals(user.getId())) {
            throw new UnauthorizedActionException("Unauthorized to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }


}
