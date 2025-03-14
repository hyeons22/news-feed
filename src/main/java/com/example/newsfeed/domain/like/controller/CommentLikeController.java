package com.example.newsfeed.domain.like.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.like.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 댓글 좋아요
    @PostMapping("/likes/comments/{commentId}/posts/{postId}")
    public ResponseEntity<String> createLike(
            @Auth AuthUser authUser,
            @PathVariable Long commentId,
            @PathVariable Long postId
    ) {
        commentLikeService.createLike(authUser, commentId, postId);
        return new ResponseEntity<>("댓글에 좋아요를 눌렀습니다.", HttpStatus.OK);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/likes/comments/{commentId}/posts/{postId}")
    public ResponseEntity<String> deleteLike(
            @Auth AuthUser authUser,
            @PathVariable Long commentId,
            @PathVariable Long postId
    ) {
        commentLikeService.deleteLike(authUser, commentId, postId);
        return new ResponseEntity<>("댓글에 좋아요를 취소했습니다.", HttpStatus.OK);
    }
}
