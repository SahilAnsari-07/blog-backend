package com.example.blog.mapper;

import com.example.blog.dto.CommentRequest;

import com.example.blog.dto.CommentResponse;
import com.example.blog.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentRequest commentRequest);
    CommentResponse toResponse(Comment comment);

}
