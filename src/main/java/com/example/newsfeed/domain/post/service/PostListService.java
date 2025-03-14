package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.response.PostDateResponseDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostListService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 팔로우한 유저 게시물 전체 조회
    @Transactional(readOnly = true)
    public Page<PostDateResponseDto> findAllPostsByFollow(AuthUser authUser, int page, int size) {
        User follower = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Post> postPage = postRepository.findAllPostsByFollow(follower.getId(), pageable);

        List<PostDateResponseDto> dtoList = postPage.getContent().stream()
                .map(PostDateResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }

    // 게시물 전체 조회 (수정일기준)
    @Transactional(readOnly = true)
    public Page<PostDateResponseDto> findAllPostsSortedByModified(AuthUser authUser, int page, int size) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size, Sort.by("modifiedAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostDateResponseDto> dtoList = postPage.getContent().stream()
                .map(PostDateResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }

    // 게시물 전체 조회 (기간별 검색)
    @Transactional(readOnly = true)
    public Page<PostDateResponseDto> findAllPostsSortedByPeriodRange(AuthUser authUser, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Post> postPage = postRepository.findAllPostsSortedByPeriodRange(startDate, endDate, pageable);

        List<PostDateResponseDto> dtoList = postPage.getContent().stream()
                .map(PostDateResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }
}
