package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.user WHERE p.status = :status")
    List<Post> findByStatus(PostStatus status);

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.user")
    List<Post> findAll();

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.user WHERE p.id = :id")
    Optional<Post> findById(@Param("id") Long id);


}