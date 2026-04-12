package com.example.blog.mapper;

import com.example.blog.dto.PostRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostResponse toResponse(Post post);

    Post toEntity(PostRequest request);
    void updateEntityFromDto(PostRequest request, @MappingTarget Post post);
}