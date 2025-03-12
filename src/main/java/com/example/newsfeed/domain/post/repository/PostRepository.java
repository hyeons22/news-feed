package com.example.newsfeed.domain.post.repository;

import com.example.newsfeed.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    Page<Post> findMyPostsByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    Page<Post> findAllByCreatedAt(Pageable pageable);
}
