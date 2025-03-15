package com.example.newsfeed.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
