package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.user.dto.response.UserMyProfileResponseDto;
import com.example.newsfeed.domain.user.dto.response.UserProfileResponseDto;
import com.example.newsfeed.domain.user.service.UserService;
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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(userService.findAllUsers(page,size));
    }

    // 다른 유저 조회(다른 사용자 프로필 조회)
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProfileResponseDto> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findProfile(userId));
    }

    // 본인 프로필 조회
    @GetMapping("/users/me")
    public ResponseEntity<UserMyProfileResponseDto> getMyProfile(){
        return ResponseEntity.ok(userService.findMyProfile());
    }

    // 본인 프로필 수정
    @PatchMapping("/users/profile")
    public ResponseEntity<UserMyProfileResponseDto> updateMyProfile(){
        return ResponseEntity.ok(userService.updateMyProfile());
    }

    // 본인 비밀번호 수정
    @PatchMapping("/users/password")
    public ResponseEntity<String> changePassword(){
        userService.changePassword();
        return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
    }

    // 유저 삭제
    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(){
        userService.deleteUser();
        return new ResponseEntity<>("유저가 삭제되었습니다.", HttpStatus.OK);
    }
}
