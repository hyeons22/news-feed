package com.example.newsfeed.domain.user.dto.response;

import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {

    private final Long id;

    private final String email;

    private final String info;

    private final String mbti;

    public UserProfileResponseDto(Long id, String email, String info, String mbti) {
        this.id = id;
        this.email = email;
        this.info = info;
        this.mbti = mbti;
    }

    public static UserProfileResponseDto toDto(User user) {
        return new UserProfileResponseDto(
                user.getId(),
                user.getEmail(),
                user.getInfo(),
                user.getMbti()
        );
    }
}
