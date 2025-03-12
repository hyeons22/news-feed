package com.example.newsfeed.domain.post.dto.response;

import com.example.newsfeed.domain.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long id;

    private final Long userId;

    private final String title;

    private final String content;

    public PostResponseDto(Long id, Long userId, String title, String content) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    public static PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getContent()
        );
    }
}
