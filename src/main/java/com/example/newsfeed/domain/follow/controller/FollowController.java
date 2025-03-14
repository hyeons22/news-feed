package com.example.newsfeed.domain.follow.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.follow.service.FollowService;
import com.example.newsfeed.domain.user.dto.response.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    // 팔로우
    @PostMapping("/follows/users/{userId}")
    public ResponseEntity<String> followUser(
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ) {
        followService.followUser(authUser, userId);
        return new ResponseEntity<>("팔로우 되었습니다.", HttpStatus.OK);
    }

    // 팔로우 목록 조회
    @GetMapping("/follows/users")
    public ResponseEntity<Page<UserProfileResponseDto>> getAllFollows(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(followService.findAllFollows(authUser, page, size));
    }

    // 언팔로우
    @DeleteMapping("/follows/users/{userId}")
    public ResponseEntity<String> unfollowUser(
            @Auth AuthUser authUser,
            @PathVariable Long userId
    ) {
        followService.unfollowUser(authUser, userId);
        return new ResponseEntity<>("팔로우가 해제되었습니다.", HttpStatus.OK);
    }
}
