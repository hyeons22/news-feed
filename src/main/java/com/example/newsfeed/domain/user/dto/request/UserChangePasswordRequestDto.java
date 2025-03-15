package com.example.newsfeed.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequestDto {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$",
            message = "새 비밀번호는 8자 이상이어야 하고, 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String newPassword;

    @NotBlank
    private String newPasswordCheck;
}
