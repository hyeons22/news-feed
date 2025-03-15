package com.example.newsfeed.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateMyProfileRequestDto {

    private String info;

    private String mbti;
}
