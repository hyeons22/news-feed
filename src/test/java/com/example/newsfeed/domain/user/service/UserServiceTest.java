package com.example.newsfeed.domain.user.service;

import com.example.newsfeed.config.PasswordEncoder;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.user.dto.request.UserChangePasswordRequestDto;
import com.example.newsfeed.domain.user.dto.request.UserUpdateMyProfileRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserMyProfileResponseDto;
import com.example.newsfeed.domain.user.dto.response.UserProfileResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findAllUser_유저_전체_조회_가능() {
        // given
        String password = "Asdf1234!";
        User user1 = new User("홍길동1", "aaa@aaa1.com", password, "자기소개", "isfp");
        User user2 = new User("홍길동2", "aaa@aaa2.com", password, "자기소개", "isfp");
        User user3 = new User("홍길동3", "aaa@aaa3.com", password, "자기소개", "isfp");

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(0, size);
        List<User> userList = List.of(user1, user2, user3);
        Page<User> userPage = new PageImpl<>(userList, pageable, 3);

        given(userRepository.findAll(pageable)).willReturn(userPage);

        // when
        Page<UserProfileResponseDto> result = userService.findAllUsers(page, size);

        // then
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertEquals(1, result.getTotalPages());
        assertEquals(3, result.getContent().size());
        assertEquals("aaa@aaa1.com", result.getContent().get(0).getEmail());

    }

    @Test
    void findProfile_다른_유저_조회를_할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user2Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user2, "id", user2Id);

        given(userRepository.findById(user2Id)).willReturn(Optional.of(user2));

        // when
        UserProfileResponseDto userProfileResponseDto = userService.findProfile(authUser, user2Id);

        // then
        assertNotNull(userProfileResponseDto);
        assertEquals(userProfileResponseDto.getEmail(), user2.getEmail());
    }

    @Test
    void findMyProfile_본인만_본인프로필을_조회할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));

        // when
        UserMyProfileResponseDto userMyProfileResponseDto = userService.findMyProfile(authUser);

        // then
        assertNotNull(userMyProfileResponseDto);
        assertEquals(userMyProfileResponseDto.getId(), user1.getId());
        assertEquals(userMyProfileResponseDto.getEmail(), user1.getEmail());
    }

    @Test
    void updateMyProfile_본인만_본인프로필_수정이_가능하다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        UserUpdateMyProfileRequestDto requestDto = new UserUpdateMyProfileRequestDto("자기소개 수정", "esfj");

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));

        // when
        UserMyProfileResponseDto responseDto = userService.updateMyProfile(authUser, requestDto);

        //then
        assertNotNull(responseDto);
        assertEquals(responseDto.getInfo(), requestDto.getInfo());
        assertEquals(responseDto.getMbti(), requestDto.getMbti());
    }

    @Test
    void changePassword_본인만_본인계정의_비밀번호를_수정할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        UserChangePasswordRequestDto requestDto = new UserChangePasswordRequestDto("Asdf1234!", "Asdf123456!", "Asdf123456!");

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(passwordEncoder.matches(requestDto.getOldPassword(), user1.getPassword())).willReturn(true);
        given(passwordEncoder.encode(requestDto.getNewPassword())).willReturn("encodedPassword");

        // when
        userService.changePassword(authUser, requestDto);

        // then
        assertEquals(user1.getPassword(), "encodedPassword");
    }


}