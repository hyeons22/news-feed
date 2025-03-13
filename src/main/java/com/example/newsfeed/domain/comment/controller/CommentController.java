package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comments/posts/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(
            @Auth AuthUser authUser,
            @Valid @RequestBody CommentRequestDto requestDto,
            @PathVariable Long postId
    ){
        return ResponseEntity.ok(commentService.createComment(authUser, requestDto, postId));
    }

    // 댓글 단건 조회
    @GetMapping("/comments/{commentId}/posts/{postId}")
    public ResponseEntity<CommentResponseDto> getComment(
            @Auth AuthUser authUser,
            @PathVariable Long commentId,
            @PathVariable Long postId
    ){
        return ResponseEntity.ok(commentService.findCommentById(authUser, commentId, postId));
    }

    // 본인 댓글 수정
    @PatchMapping("/comments/{commentId}/posts/{postId}")
    public ResponseEntity<CommentResponseDto> updateMyComment(
            @Auth AuthUser authUser,
            @PathVariable Long commentId,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto requestDto
    ){
        return ResponseEntity.ok(commentService.updateMyComment(authUser, commentId, postId, requestDto));
    }

    // 본인 댓글 삭제
    @DeleteMapping("/comments/{commentId}/posts/{postId}")
    public ResponseEntity<String> deleteMyComment(
            @Auth AuthUser authUser,
            @PathVariable Long commentId,
            @PathVariable Long postId
    ){
        commentService.deleteMyComment(authUser, commentId, postId);
        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

}
