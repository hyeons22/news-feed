package com.example.newsfeed.domain.comment.dto.response;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long id;

    private final Long userId;

    private final Long postId;

    private final String content;

    public CommentResponseDto(Long id, Long userId, Long postId, String content) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }
}
