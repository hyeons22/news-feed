package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.dto.response.PostDateResponseDto;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(AuthUser authUser, @Valid PostSaveRequestDto dto) {
        User user = userCheckValidation(authUser);

        Post post = new Post(dto.getTitle(), dto.getContent(), user);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(
                savedPost.getId(),
                savedPost.getUser().getId(),
                savedPost.getTitle(),
                savedPost.getContent()
        );
    }

    // 본인 게시물 전체 조회
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findMyPosts(AuthUser authUser, int page, int size) {
        User user = userCheckValidation(authUser);

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Post> postPage = postRepository.findMyPostsByUserId(authUser.getUserId(), pageable);

        List<PostResponseDto> dtoList = postPage.getContent().stream()
                .map(PostResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }

    // 게시물 전체 조회
    @Transactional(readOnly = true)
    public Page<PostDateResponseDto> findAll(AuthUser authUser, int page, int size) {
        User user = userCheckValidation(authUser);

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Post> postPage = postRepository.findAllByCreatedAt(pageable);

        List<PostDateResponseDto> dtoList = postPage.getContent().stream()
                .map(PostDateResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }

    // 본인 게시물 수정
    @Transactional
    public PostResponseDto updateMyPost(AuthUser authUser, Long postId, @Valid PostUpdateRequestDto dto) {
        User user = userCheckValidation(authUser);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (!Objects.equals(user.getId(), post.getUser().getId())) {
            throw new IllegalArgumentException("본인 게시물이 아닙니다.");
        }

        post.update(dto.getTitle(), dto.getContent());

        return new PostResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getContent()
        );
    }

    // 본인 게시물 삭제
    @Transactional
    public void deleteMyPost(AuthUser authUser, Long postId) {
        User user = userCheckValidation(authUser);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        if (!Objects.equals(user.getId(), post.getUser().getId())) {
            throw new IllegalArgumentException("본인 게시물이 아닙니다.");
        }

        postRepository.deleteById(postId);
    }

    // 유저 확인
    private User userCheckValidation(AuthUser authUser) {
        return userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );
    }
}
