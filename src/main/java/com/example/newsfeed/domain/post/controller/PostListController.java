package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.response.PostDateResponseDto;
import com.example.newsfeed.domain.post.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostListController {

    private final PostListService postListService;

    // 팔로우한 유저 게시물 전체 조회
    @GetMapping("/posts/sorted-follow")
    public ResponseEntity<Page<PostDateResponseDto>> getAllPostsByFollow(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(postListService.findAllPostsByFollow(authUser, page, size));
    }

    // 게시물 전체 조회 (수정일기준)
    @GetMapping("/posts/sorted-by-modified")
    public ResponseEntity<Page<PostDateResponseDto>> getAllPostsSortedByModified(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(postListService.findAllPostsSortedByModified(authUser, page, size));
    }

    // 게시물 전체 조회 (기간별 검색)
    @GetMapping("/posts/sorted-by-period-range")
    public ResponseEntity<Page<PostDateResponseDto>> getAllPostsSortedByPeriodRange(
            @Auth AuthUser authUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(postListService.findAllPostsSortedByPeriodRange(authUser, startDate, endDate, page, size));
    }
}
