package com.example.blog.repository;

import com.example.blog.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByParentId(String parentId);
}