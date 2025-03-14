package com.example.newsfeed.domain.like.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.like.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 게시물 좋아요
    @PostMapping("/likes/posts/{postId}")
    public ResponseEntity<String> createLike(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ){
        postLikeService.createLike(authUser, postId);
        return new ResponseEntity<>("게시물에 좋아요를 눌렀습니다.", HttpStatus.OK);
    }

    // 게시물 좋아요 삭제
    @DeleteMapping("/likes/posts/{postId}")
    public ResponseEntity<String> deleteLike(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ){
        postLikeService.deleteLike(authUser, postId);
        return new ResponseEntity<>("게시물에 좋아요를 취소했습니다.", HttpStatus.OK);
    }
}
