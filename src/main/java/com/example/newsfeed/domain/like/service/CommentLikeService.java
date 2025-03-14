package com.example.newsfeed.domain.like.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.like.entity.CommentLike;
import com.example.newsfeed.domain.like.repository.CommentLikeRepository;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 좋아요
    @Transactional
    public void createLike(AuthUser authUser, Long commentId, Long postId) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if (user.getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("본인 댓글에는 좋아요를 누를 수 없습니다.");
        }

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException("게시물에 해당 댓글이 없습니다.");
        }

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByUserIdAndCommentId(user.getId(), commentId);

        if (optionalCommentLike.isPresent()) {
            CommentLike commentLike = optionalCommentLike.get();

            if (commentLike.isLike_status()) {
                throw new IllegalArgumentException("이미 좋아요를 누른 댓글입니다.");
            }

            commentLike.update(true);
        }

        if (optionalCommentLike.isEmpty()) {
            CommentLike commentLike = new CommentLike(user, comment, true);
            commentLikeRepository.save(commentLike);
        }
    }

    // 댓글 좋아요 취소
    @Transactional
    public void deleteLike(AuthUser authUser, Long commentId, Long postId) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if (user.getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("본인 댓글에는 좋아요 취소를 누를 수 없습니다.");
        }

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IllegalArgumentException("게시물에 해당 댓글이 없습니다.");
        }

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByUserIdAndCommentId(user.getId(), commentId);

        if (optionalCommentLike.isPresent()) {
            CommentLike commentLike = optionalCommentLike.get();

            if (!commentLike.isLike_status()) {
                throw new IllegalArgumentException("좋아요를 누르지 않은 댓글입니다.");
            }

            commentLike.update(false);
        }
    }
}
