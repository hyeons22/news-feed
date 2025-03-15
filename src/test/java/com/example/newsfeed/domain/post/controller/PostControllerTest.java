package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.service.PostService;
import com.example.newsfeed.domain.user.controller.UserController;
import com.example.newsfeed.domain.user.service.UserService;
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

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    void createPost_게시물_생성_성공() throws Exception {
        // given
        Long authUserId = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");

        Long postId = 1L;
        PostSaveRequestDto requestDto = new PostSaveRequestDto("제목", "내용");
        PostResponseDto responseDto = new PostResponseDto(postId, authUserId, requestDto.getTitle(), requestDto.getContent());

        given(postService.createPost(any(AuthUser.class), any(PostSaveRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.userId").value(responseDto.getUserId()))
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.content").value(responseDto.getContent()));
    }


}