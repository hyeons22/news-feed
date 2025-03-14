package com.example.newsfeed.domain.like.repository;

import com.example.newsfeed.domain.like.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);
}
