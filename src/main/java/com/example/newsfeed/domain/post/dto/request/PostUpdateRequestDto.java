package com.example.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
