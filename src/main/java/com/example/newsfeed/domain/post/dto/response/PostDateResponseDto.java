package com.example.newsfeed.domain.post.dto.response;

import com.example.newsfeed.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDateResponseDto {

    private final Long id;

    private final Long userId;

    private final String title;

    private final String content;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public PostDateResponseDto(Long id, Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static PostDateResponseDto toDto(Post post) {
        return new PostDateResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
