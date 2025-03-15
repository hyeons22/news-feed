package com.example.newsfeed.domain.auth.service;

import com.example.newsfeed.config.JwtUtil;
import com.example.newsfeed.config.PasswordEncoder;
import com.example.newsfeed.domain.auth.dto.request.LoginRequestDto;
import com.example.newsfeed.domain.auth.dto.request.SignupRequestDto;
import com.example.newsfeed.domain.auth.dto.response.LoginResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup_회원가입을_할_수_있다() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("홍길동", "aaa@aaa.com", "Asdf1234!", "자기소개", "isfp");

        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(false);

        // when
        authService.signup(signupRequestDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_로그인을_할_수_있다() {
        // given
        String password = "Asdf1234!";
        User user = new User("홍길동", "aaa@aaa.com", password, "자기소개", "isfp");
        ReflectionTestUtils.setField(user, "id", 1L);

        LoginRequestDto loginRequestDto = new LoginRequestDto("aaa@aaa.com", password);

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).willReturn(true);
        given(jwtUtil.createToken(user.getId(), user.getEmail())).willReturn(bearerToken);

        // when
        LoginResponseDto responseDto = authService.login(loginRequestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(bearerToken).isEqualTo(responseDto.getBearJwt());
    }

    @Test
    void 회원가입시_이메일이_이미_존재하면_IllegalArgumentException을_던진다() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("홍길동", "aaa@aaa.com", "Asdf1234!", "자기소개", "isfp");
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.signup(signupRequestDto));
    }

    @Test
    void 로그인시_이메일이_존재하지_않으면_IllegalArgumentException을_던진다() {
        // given
        String password = "Asdf1234!";
        User user = new User("홍길동", "aaa@aaa.com", password, "자기소개", "isfp");
        LoginRequestDto loginRequestDto = new LoginRequestDto("aaa@aaa.com", password);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequestDto));
    }

    @Test
    void 로그인시_비밀번호가_일치하지_않으면_IllegalArgumentException을_던진다() {
        // given
        String password = "Asdf1234!";
        String encodedPassword = passwordEncoder.encode(password);
        String wrongPassword = "Asdf123456!";
        User user = new User("홍길동", "aaa@aaa.com", encodedPassword, "자기소개", "isfp");
        LoginRequestDto loginRequestDto = new LoginRequestDto("aaa@aaa.com", wrongPassword);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequestDto));
    }
}