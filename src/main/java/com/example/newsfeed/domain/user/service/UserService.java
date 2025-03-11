package com.example.newsfeed.domain.user.service;

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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
    public UserProfileResponseDto findProfile(Long userId) {
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
    public UserMyProfileResponseDto findMyProfile() {
        // 로그인시 본인확인
        return null;
    }

    // 본인 프로필 수정
    @Transactional
    public UserMyProfileResponseDto updateMyProfile() {
        return null;
    }

    // 본인 비밀번호 수정
    @Transactional
    public void changePassword() {
    }

    // 유저 삭제
    @Transactional
    public void deleteUser() {
    }
}
