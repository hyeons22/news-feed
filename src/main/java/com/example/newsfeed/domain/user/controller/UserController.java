package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.user.dto.request.UserChangePasswordRequestDto;
import com.example.newsfeed.domain.user.dto.request.UserUpdateMyProfileRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserMyProfileResponseDto;
import com.example.newsfeed.domain.user.dto.response.UserProfileResponseDto;
import com.example.newsfeed.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 유저 전체 조회 (페이징)
    @GetMapping("/users")
    public ResponseEntity<Page<UserProfileResponseDto>> getAllUsers(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(userService.findAllUsers(page,size));
    }

    // 다른 유저 단건 조회(다른 사용자 프로필 조회)
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProfileResponseDto> getProfile(
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.findProfile(authUser, userId));
    }

    // 본인 프로필 조회
    @GetMapping("/users/me")
    public ResponseEntity<UserMyProfileResponseDto> getMyProfile(@Auth AuthUser authUser){
        return ResponseEntity.ok(userService.findMyProfile(authUser));
    }

    // 본인 프로필 수정
    @PatchMapping("/users/my-profile")
    public ResponseEntity<UserMyProfileResponseDto> updateMyProfile(
            @Auth AuthUser authUser,
            @RequestBody UserUpdateMyProfileRequestDto requestDto
    ){
        return ResponseEntity.ok(userService.updateMyProfile(authUser, requestDto));
    }

    // 본인 비밀번호 수정
    @PatchMapping("/users/change-password")
    public ResponseEntity<String> changePassword(
            @Auth AuthUser authUser,
            @Valid @RequestBody UserChangePasswordRequestDto requestDto
    ){
        userService.changePassword(authUser, requestDto);
        return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    // 유저 삭제
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(@Auth AuthUser authUser){
        userService.deleteUser(authUser);
        return new ResponseEntity<>("유저가 삭제되었습니다.", HttpStatus.OK);
    }
}
