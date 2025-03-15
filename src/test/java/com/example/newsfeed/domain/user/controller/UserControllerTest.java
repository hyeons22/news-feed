package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.auth.controller.AuthController;
import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.user.dto.response.UserMyProfileResponseDto;
import com.example.newsfeed.domain.user.dto.response.UserProfileResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @Test
    void getProfile_다른_유저_단건_조회_성공() throws Exception{
        // given
        Long authUserId = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        Long user2Id = 2L;
        String password = "Asdf1234!";
        User user2 = new User("홍길동2", "aaa@aaa2.com", password, "자기소개", "isfp");
        UserProfileResponseDto responseDto = new UserProfileResponseDto(user2.getId(), user2.getEmail(),  user2.getInfo(), user2.getMbti());

        given(userService.findProfile(any(AuthUser.class), any(Long.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/users/{userId}", user2Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()));

    }

    @Test
    void getMyProfile_본인_프로필_조회_성공() throws Exception {
        // given
        Long authUserId = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        Long user1Id = 1L;
        String password = "Asdf1234!";
        User user1 = new User("홍길동1", "aaa@aaa1.com", password, "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        UserMyProfileResponseDto responseDto = new UserMyProfileResponseDto(user1.getId(), user1.getName(), user1.getEmail(), user1.getInfo(), user1.getMbti());

        given(userService.findMyProfile(any(AuthUser.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }
}