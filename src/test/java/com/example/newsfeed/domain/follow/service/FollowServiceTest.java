package com.example.newsfeed.domain.follow.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.follow.entity.Follow;
import com.example.newsfeed.domain.follow.repository.FollowRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    void followUser_팔로우를_할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user2, "id", 2L);

        Follow follow = new Follow(user1, user2, false);

        given(userRepository.findById(authUserId)).willReturn(Optional.of(user1));
        given(userRepository.findById(user2.getId())).willReturn(Optional.of(user2));
        given(followRepository.findByFollowerIdAndFollowingId(authUserId, user2.getId())).willReturn(Optional.of(follow));
        given(followRepository.save(any(Follow.class))).willReturn(follow);

        // when
        followService.followUser(authUser, user2.getId());

        // then
        assertEquals(follow.getFollower().getId(), user1.getId());
        assertEquals(follow.getFollowing().getId(), user2.getId());
        assertThat(follow.isFollow_status()).isTrue();
    }

}