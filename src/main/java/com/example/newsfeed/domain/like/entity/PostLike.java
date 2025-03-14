package com.example.newsfeed.domain.like.entity;

import com.example.newsfeed.common.entity.DateEntity;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "postlikes")
public class PostLike extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Post_id", nullable = false)
    private Post post;

    private boolean like_status;

    public PostLike(User user, Post post, boolean like_status) {
        this.user = user;
        this.post = post;
        this.like_status = like_status;
    }

    public void update(boolean like_status) {
        this.like_status = like_status;
    }
}
