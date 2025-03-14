package com.example.newsfeed.domain.like.entity;

import com.example.newsfeed.common.entity.DateEntity;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "commentlikes")
public class CommentLike extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    private boolean like_status;

    public CommentLike(User user, Comment comment, boolean like_status) {
        this.user = user;
        this.comment = comment;
        this.like_status = like_status;
    }

    public void update(boolean like_status) {
        this.like_status = like_status;
    }
}
