package com.example.newsfeed.domain.user.service;

import com.example.newsfeed.common.annotation.Auth;
import com.example.newsfeed.config.PasswordEncoder;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.user.dto.request.UserChangePasswordRequestDto;
import com.example.newsfeed.domain.user.dto.request.UserUpdateMyProfileRequestDto;
import com.example.newsfeed.domain.user.dto.response.UserMyProfileResponseDto;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 전체 조회 (페이징)
    @Transactional
    public Page<UserProfileResponseDto> findAllUsers(int page, int size) {
        int adjustPage = (page > 0) ? page - 1 : 0;
        Pageable pageable = PageRequest.of(adjustPage, size);
        Page<User> userPage = userRepository.findAll(pageable);

        List<UserProfileResponseDto> dtoList = userPage.getContent().stream()
                .map(UserProfileResponseDto::toDto)
                .toList();

        return new PageImpl<>(dtoList, pageable, userPage.getTotalElements());
    }

    // 다른 유저 조회(다른 사용자 프로필 조회)
    @Transactional(readOnly = true)
    public UserProfileResponseDto findProfile(AuthUser authUser, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );

        return new UserProfileResponseDto(
                user.getId(),
                user.getEmail(),
                user.getInfo(),
                user.getMbti()
        );
    }

    // 본인 프로필 조회
    @Transactional(readOnly = true)
    public UserMyProfileResponseDto findMyProfile(AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );

        return new UserMyProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getInfo(),
                user.getMbti()
        );
    }

    // 본인 프로필 수정
    @Transactional
    public UserMyProfileResponseDto updateMyProfile(AuthUser authUser, UserUpdateMyProfileRequestDto requestDto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );

        if (requestDto.getInfo() != null) {
            user.updateInfo(requestDto.getInfo());
        }

        if (requestDto.getMbti() != null) {
            user.updateMbti(requestDto.getMbti());
        }

        return new UserMyProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getInfo(),
                user.getMbti()
        );
    }

    // 본인 비밀번호 수정
    @Transactional
    public void changePassword(AuthUser authUser, UserChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );

        // 비밀번호 수정시, 본인 확인 (비밀번호 일치 여부 확인)
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 현재 비밀번호와 새 비밀번호가 같은지 확인
        if (requestDto.getOldPassword().equals(requestDto.getNewPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 동일합니다.");
        }

        // 새 비밀번호와 새 비밀번호 확인 번호가 일치하는지 확인
        if (!requestDto.getNewPassword().equals(requestDto.getNewPasswordCheck())) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인 번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getNewPassword());

        user.updatePassword(encodedPassword);
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );

        userRepository.deleteById(user.getId());
    }
}
