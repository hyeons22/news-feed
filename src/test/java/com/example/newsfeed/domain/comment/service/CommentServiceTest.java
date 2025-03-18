package com.example.newsfeed.domain.comment.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_댓글을_등록할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user2, "id", 2L);

        Long postId = 1L;
        Post post = new Post("제목", "내용", user2);
        ReflectionTestUtils.setField(post, "id", postId);

        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");
        Comment comment = new Comment(requestDto.getContent(), user1, post);
        ReflectionTestUtils.setField(comment, "id", 1L);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        CommentResponseDto responseDto = commentService.createComment(authUser, requestDto, postId);

        // then
        assertNotNull(responseDto);
        assertEquals(comment.getContent(), responseDto.getContent());
    }

    @Test
    void findCommentById_댓글_단건_조회를_할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user2, "id", 2L);

        Long postId = 1L;
        Post post = new Post("제목", "내용", user2);
        ReflectionTestUtils.setField(post, "id", postId);

        Comment comment = new Comment("댓글 내용", user1, post);
        ReflectionTestUtils.setField(comment, "id", 1L);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(comment));

        // when
        CommentResponseDto responseDto = commentService.findCommentById(authUser, comment.getId(), post.getId());

        // then
        assertEquals(comment.getId(), responseDto.getId());
        assertEquals(comment.getUser().getId(), responseDto.getUserId());
        assertEquals(comment.getContent(), responseDto.getContent());
    }

    @Test
    void updateMyComment_본인_댓글_수정할_수_있다() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user2, "id", 2L);

        Long postId = 1L;
        Post post = new Post("제목", "내용", user2);
        ReflectionTestUtils.setField(post, "id", postId);

        Comment comment = new Comment("댓글 내용", user1, post);
        ReflectionTestUtils.setField(comment, "id", 1L);

        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용 수정");

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(comment));

        // when
        CommentResponseDto responseDto = commentService.updateMyComment(authUser, comment.getId(), post.getId(), requestDto);

        // then
        assertNotNull(comment);
        assertEquals(comment.getContent(), responseDto.getContent());
        assertEquals(comment.getUser().getId(), responseDto.getUserId());

    }

    @Test
    void deleteMyComment_본인_댓글_삭제() {
        // given
        Long authUserId = 1L;
        Long user1Id = 1L;
        AuthUser authUser = new AuthUser(authUserId, "aaa@aaa1.com");
        User user1 = new User("홍길동1", "aaa@aaa1.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user1, "id", user1Id);

        User user2 = new User("홍길동2", "aaa@aaa2.com", "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user2, "id", 2L);

        Long postId = 1L;
        Post post = new Post("제목", "내용", user2);
        ReflectionTestUtils.setField(post, "id", postId);

        Comment comment = new Comment("댓글 내용", user1, post);
        ReflectionTestUtils.setField(comment, "id", 1L);

        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user1));
        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(post));
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(anyLong());

        // when
        commentService.deleteMyComment(authUser, comment.getId(), post.getId());

        // then
        verify(commentRepository, times(1)).deleteById(comment.getId());

    }


}