package com.example.newsfeed.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final String info;

    private final String mbti;

    public SignupResponseDto(Long id, String name, String email, String info, String mbti) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.info = info;
        this.mbti = mbti;
    }
}
