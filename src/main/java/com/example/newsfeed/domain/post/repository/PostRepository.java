package com.example.newsfeed.domain.post.repository;

import com.example.newsfeed.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 본인 게시물 조회 (최신순)
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    Page<Post> findMyPostsByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    Page<Post> findAllByCreatedAt(Pageable pageable);

    // 팔로우한사람 게시물 조회
    @Query("SELECT p FROM Post p JOIN Follow f ON p.user.id = f.following.id WHERE f.follower.id = :followerId AND f.follow_status = true")
    Page<Post> findAllPostsByFollow(
            @Param("followerId") Long followerId,
            Pageable pageable
    );

    // 게시물 전체 조회 (기간별 검색)
    @Query("SELECT p FROM Post p WHERE p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt")
    Page<Post> findAllPostsSortedByPeriodRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
