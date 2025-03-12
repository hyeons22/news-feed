package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.dto.response.PostDateResponseDto;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시물 생성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(
            @Auth AuthUser authUser,
            @Valid @RequestBody PostSaveRequestDto dto
    ){
        return ResponseEntity.ok(postService.createPost(authUser, dto));
    }

    // 본인 게시물 전체 조회
    @GetMapping("/posts/me")
    public ResponseEntity<Page<PostResponseDto>> getMyPosts(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(postService.findMyPosts(authUser, page, size));
    }

    // 게시물 전체 조회
    @GetMapping("/posts/sorted-by-created")
    public ResponseEntity<Page<PostDateResponseDto>> getAll(
            @Auth AuthUser authUser,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(postService.findAll(authUser, page, size));
    }

    // 본인 게시물 수정
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updateMyPost(
            @Auth AuthUser authUser,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDto dto
    ){
        return ResponseEntity.ok(postService.updateMyPost(authUser, postId, dto));
    }

    // 본인 게시물 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deleteMyPost(
            @Auth AuthUser authUser,
            @PathVariable Long postId
    ){
        postService.deleteMyPost(authUser, postId);
        return new ResponseEntity<>("게시물이 삭제되었습니다.", HttpStatus.OK);
    }
}
