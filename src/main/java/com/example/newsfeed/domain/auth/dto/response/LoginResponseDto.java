package com.example.newsfeed.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private final String bearJwt;

    public LoginResponseDto(String bearJwt) {
        this.bearJwt = bearJwt;
    }
}
