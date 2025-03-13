package com.example.newsfeed.domain.follow.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.follow.repository.FollowRepository;
import com.example.newsfeed.domain.user.dto.response.UserProfileResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    @Transactional
    public void followUser(AuthUser authUser, Long userId) {
        User follower = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        User following = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("팔로우할 유저가 존재하지 않습니다.")
        );

        if (authUser.getUserId().equals(userId)) {
            throw new IllegalArgumentException("자기자신을 팔로우할 수 없습니다.");
        }

        Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndFollowingId(follower.getId(), following.getId());
        if (optionalFollow.isPresent()) {
            Follow findFollow = optionalFollow.get();

            if (findFollow.isFollow_status()) {
                throw new IllegalArgumentException("이미 팔로우한 상태입니다.");
            }
            findFollow.update(true);
        }

        Follow follow = new Follow(follower, following, true);
        followRepository.save(follow);
    }

    // 팔로우 목록 조회
    @Transactional(readOnly = true)
    public Page<UserProfileResponseDto> findAllFollows(AuthUser authUser, int page, int size) {
        User follower = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<Follow> followPage = followRepository.findAllFollowAndStatus(follower.getId(), pageable);

        List<UserProfileResponseDto> dtoList = followPage.stream()
                .map(Follow::getFollowing)
                .map(UserProfileResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, followPage.getTotalElements());

    }

    // 언팔로우
    @Transactional
    public void unfollowUser(AuthUser authUser, Long userId) {
        User follower = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        User unfollowing = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("언팔로우할 유저가 존재하지 않습니다.")
        );

        Optional<Follow> optionalFollow = followRepository.findByFollowerIdAndFollowingId(follower.getId(), unfollowing.getId());
        if (optionalFollow.isPresent()) {
            Follow findFollow = optionalFollow.get();

            if (!findFollow.isFollow_status()) {
                throw new IllegalArgumentException("팔로우하지 않는 상태입니다..");
            }
            findFollow.update(false);
        }
    }


}
