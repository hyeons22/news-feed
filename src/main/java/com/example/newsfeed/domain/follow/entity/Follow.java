package com.example.newsfeed.domain.follow.entity;

import com.example.newsfeed.common.entity.DateEntity;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "follows")
public class Follow extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우하는 사람(팔로워)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    // 팔로우 당하는 사람 팔로잉
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    private boolean follow_status;

    public Follow(User follower, User following, boolean follow_status) {
        this.follower = follower;
        this.following = following;
        this.follow_status = follow_status;
    }

    public void update(boolean follow_status) {
        this.follow_status = follow_status;
    }
}
