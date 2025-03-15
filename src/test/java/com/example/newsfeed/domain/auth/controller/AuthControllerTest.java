package com.example.newsfeed.domain.auth.controller;

import com.example.newsfeed.domain.auth.dto.request.LoginRequestDto;
import com.example.newsfeed.domain.auth.dto.request.SignupRequestDto;
import com.example.newsfeed.domain.auth.dto.response.LoginResponseDto;
import com.example.newsfeed.domain.auth.dto.response.SignupResponseDto;
import com.example.newsfeed.domain.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void 회원가입_성공() throws Exception {
        // given
        Long userId = 1L;
        SignupRequestDto signupRequestDto = new SignupRequestDto("홍길동", "aaa@aaa.com", "Asdf1234!", "자기소개", "isfp");
        SignupResponseDto signupResponseDto = new SignupResponseDto(userId, "홍길동", "aaa@aaa.com", "자기소개", "isfp");

        given(authService.signup(any(SignupRequestDto.class))).willReturn(signupResponseDto);

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(signupResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(signupResponseDto.getName()))
                .andExpect(jsonPath("$.email").value(signupResponseDto.getEmail()))
                .andExpect(jsonPath("$.info").value(signupResponseDto.getInfo()))
                .andExpect(jsonPath("$.mbti").value(signupResponseDto.getMbti()));

    }

    @Test
    void 로그인_성공() throws Exception{
        // given
        Long userId = 1L;
        String bearJwt = "test-bearer-token";
        LoginRequestDto loginRequestDto = new LoginRequestDto("aaa@aaa.com", "Asdf1234!");
        LoginResponseDto loginResponseDto = new LoginResponseDto(bearJwt);

        given(authService.login(any(LoginRequestDto.class))).willReturn(loginResponseDto);

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearJwt").value(bearJwt));
    }

}