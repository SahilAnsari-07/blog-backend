package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.PostStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"category", "user"})
    List<Post> findByUserId(Long userId);


    @EntityGraph(attributePaths = {"category", "user"})
    Page<Post> findByStatus(PostStatus status, Pageable pageable);




    @EntityGraph(attributePaths = {"category", "user"})
    @Query("SELECT p FROM Post p")
    Page<Post> findAllPosts(Pageable pageable);



    @EntityGraph(attributePaths = {"category", "user"})
    Optional<Post> findById(Long id);



}