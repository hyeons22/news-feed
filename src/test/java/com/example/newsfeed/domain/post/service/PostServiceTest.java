package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.dto.response.PostDateResponseDto;
import com.example.newsfeed.domain.post.dto.response.PostResponseDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void creatPost_게시물_생성_가능() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        PostSaveRequestDto requestDto = new PostSaveRequestDto("제목1", "내용1");

        Post post = new Post(requestDto.getTitle(), requestDto.getContent(), user1);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        PostResponseDto responseDto = postService.createPost(authUser, requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(post.getId(), responseDto.getId());
        assertEquals("제목1", responseDto.getTitle());
    }

    @Test
    void findMyPosts_본인_게시물_전체_조회_가능() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        Post post1 = new Post("제목1", "내용1", user1);
        Post post2 = new Post("제목2", "내용2", user1);
        Post post3 = new Post("제목3", "내용3", user1);

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Post> postList = List.of(post1, post2, post3);
        Page<Post> postPage = new PageImpl<>(postList, pageable, 3);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findMyPostsByUserId(authUserId, pageable)).willReturn(postPage);

        // when
        Page<PostResponseDto> responseDto = postService.findMyPosts(authUser, page, size);

        // then
        assertEquals(3, responseDto.getTotalElements());
        assertEquals(user1.getId(), responseDto.getContent().get(1).getUserId());
    }

    @Test
    void findAll_게시물_전체_조회() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");

        Post post1 = new Post("제목1", "내용1", user1);
        Post post2 = new Post("제목2", "내용2", user1);
        Post post3 = new Post("제목3", "내용3", user1);
        Post post4 = new Post("제목4", "내용4", user2);

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Post> postList = List.of(post1, post2, post3, post4);
        Page<Post> postPage = new PageImpl<>(postList, pageable, 4);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findAllByCreatedAt(pageable)).willReturn(postPage);

        // when
        Page<PostDateResponseDto> responseDto= postService.findAll(authUser, page, size);

        // then
        assertEquals(4, responseDto.getTotalElements());
        assertEquals(user2.getId(), responseDto.getContent().get(3).getUserId());
    }

    @Test
    void updateMyPost_본인_게시물_수정_가능() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        Post post = new Post("제목1", "내용1", user1);
        ReflectionTestUtils.setField(post, "id", 1L);

        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("제목 수정", "내용 수정");

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));

        // when
        PostResponseDto responseDto = postService.updateMyPost(authUser, post.getId(), requestDto);

        // then
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getContent(), responseDto.getContent());
    }

    @Test
    void deleteByPost_본인_게시물_삭제_가능() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        Post post = new Post("제목1", "내용1", user1);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(anyLong());

        // when
        postService.deleteMyPost(authUser, post.getId());

        // then
        verify(postRepository, times(1)).deleteById(post.getId());
    }
}