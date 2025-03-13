package com.example.newsfeed.domain.comment.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.comment.dto.request.CommentRequestDto;
import com.example.newsfeed.domain.comment.dto.response.CommentResponseDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(AuthUser authUser, @Valid CommentRequestDto requestDto, Long postId) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        Comment comment = new Comment(requestDto.getContent(), user, post);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(
                savedComment.getId(),
                user.getId(),
                post.getId(),
                savedComment.getContent()
        );
    }

    // 댓글 단건 조회
    @Transactional(readOnly = true)
    public CommentResponseDto findCommentById(AuthUser authUser, Long commentId, Long postId) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        // 게시물id 와 댓글post_id가 일치하는지 확인
        if (!Objects.equals(post.getId(), comment.getPost().getId())) {
            throw new IllegalArgumentException("게시물에 검색하신 댓글이 존재하지 않습니다.");
        }

        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getPost().getId(),
                comment.getContent()
        );
    }

    // 본인 댓글 수정
    @Transactional
    public CommentResponseDto updateMyComment(AuthUser authUser, Long commentId, Long postId, CommentRequestDto requestDto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        // 게시물id 와 댓글post_id가 일치하는지 확인
        if (!Objects.equals(post.getId(), comment.getPost().getId())) {
            throw new IllegalArgumentException("게시물에 검색하신 댓글이 존재하지 않습니다.");
        }

        // 본인 댓글만 수정가능
        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new IllegalArgumentException("본인 댓글만 수정이 가능합니다.");
        }

        comment.update(requestDto.getContent());

        return new CommentResponseDto(
                comment.getId(),
                user.getId(),
                post.getId(),
                comment.getContent()
        );
    }

    // 본인 댓글 삭제
    @Transactional
    public void deleteMyComment(AuthUser authUser, Long commentId, Long postId) {

        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
        // 게시물id 와 댓글post_id가 일치하는지 확인
        if (!Objects.equals(post.getId(), comment.getPost().getId())) {
            throw new IllegalArgumentException("게시물에 검색하신 댓글이 존재하지 않습니다.");
        }

        // 본인 댓글만 수정가능
        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new IllegalArgumentException("본인 댓글만 삭제가 가능합니다.");
        }

        commentRepository.deleteById(commentId);
    }
}
