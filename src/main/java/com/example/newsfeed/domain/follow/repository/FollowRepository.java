package com.example.newsfeed.domain.follow.repository;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    Optional<Follow> findByFollowerIdAndFollowingId(
            @Param("followerId") Long followerId,
            @Param("followingId") Long followingId
    );

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId AND f.follow_status = true")
    Page<Follow> findAllFollowAndStatus(
            @Param("followerId") Long followerId,
            Pageable pageable);
}
