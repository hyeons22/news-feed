package com.example.newsfeed.domain.auth.service;

import com.example.newsfeed.config.JwtUtil;
import com.example.newsfeed.config.PasswordEncoder;
import com.example.newsfeed.domain.auth.dto.request.LoginRequestDto;
import com.example.newsfeed.domain.auth.dto.request.SignupRequestDto;
import com.example.newsfeed.domain.auth.dto.response.LoginResponseDto;
import com.example.newsfeed.domain.auth.dto.response.SignupResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto requestDto) {

        // 이메일이 존재하면 가입 x
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getName(), requestDto.getEmail(), encodedPassword, requestDto.getInfo(), requestDto.getMbti());
        userRepository.save(user);

        return new SignupResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getInfo(),
                user.getMbti()
        );
    }

    // 로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String bearJwt = jwtUtil.createToken(user.getId(), user.getEmail());
        return new LoginResponseDto(bearJwt);
    }
}
