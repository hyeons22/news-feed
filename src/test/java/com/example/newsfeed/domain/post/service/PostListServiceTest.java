package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.response.PostDateResponseDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostListServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostListService postListService;

    @Test
    void findAllPostsByFollow_팔로우한_유저_게시물_전체_조회가능() {
        // given
        Long authUserId = 1L;
        String password = "Asdf1234!";
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");

        User user = new User("홍길동1", authUser.getEmail(), password, "자기소개", "enfp");
        ReflectionTestUtils.setField(user, "id", authUserId);
        User user2 = new User("홍길동2", "aaa@aaa2.com", password, "자기소개", "isfp");

        Post post1 = new Post("제목1", "내용1", user2);
        Post post2 = new Post("제목2", "내용2", user2);
        Post post3 = new Post("제목3", "내용3", user2);

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Post> postList = List.of(post1, post2, post3);
        Page<Post> postPage = new PageImpl<>(postList, pageable, 3);

        given(userRepository.findById(authUserId)).willReturn(Optional.of(user));
        given(postRepository.findAllPostsByFollow(authUserId, pageable)).willReturn(postPage);

        // when
        Page<PostDateResponseDto> responseDto = postListService.findAllPostsByFollow(authUser, page, size);

        // then
        assertEquals(3, responseDto.getTotalElements());
        assertEquals(1, responseDto.getTotalPages());
        assertEquals("제목1", responseDto.getContent().get(0).getTitle());
    }

    @Test
    void findAllPostsSortedByModified_수정일기준_게시물_전체_조회가능() {
        // given
        Long authUserId = 1L;
        String password = "Asdf1234!";
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");

        User user = new User("홍길동1", authUser.getEmail(), password, "자기소개", "enfp");
        ReflectionTestUtils.setField(user, "id", authUserId);
        User user2 = new User("홍길동2", "aaa@aaa2.com", password, "자기소개", "isfp");

        Post post1 = new Post("제목1", "내용1", user2);
        Post post2 = new Post("제목2", "내용2", user2);
        Post post3 = new Post("제목3", "내용3", user2);
        Post post4 = new Post("제목4", "내용4", user2);

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("modifiedAt").descending());
        List<Post> postList = List.of(post1, post2, post3, post4);
        Page<Post> postPage = new PageImpl<>(postList, pageable, 4);

        given(userRepository.findById(authUserId)).willReturn(Optional.of(user));
        given(postRepository.findAll(pageable)).willReturn(postPage);

        // when
        Page<PostDateResponseDto> responseDto = postListService.findAllPostsSortedByModified(authUser, page, size);

        // then
        assertEquals(4, responseDto.getTotalElements());
        assertEquals(1, responseDto.getTotalPages());
        assertEquals("내용2", responseDto.getContent().get(1).getContent());
    }

}