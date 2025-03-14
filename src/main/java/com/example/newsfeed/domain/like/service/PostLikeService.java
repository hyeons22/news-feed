package com.example.newsfeed.domain.like.service;

import com.example.newsfeed.domain.auth.dto.response.AuthUser;
import com.example.newsfeed.domain.like.entity.PostLike;
import com.example.newsfeed.domain.like.repository.PostLikeRepository;
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
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 게시물 좋아요
    @Transactional
    public void createLike(AuthUser authUser, Long postId) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        // 본인 게시물에 좋아요를 누르는지 확인
        if (user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("본인 게시물에는 좋아요를 누룰 수 없습니다.");
        }

        Optional<PostLike> optionalPostLike = postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        if(optionalPostLike.isPresent()) {
            PostLike postLike = optionalPostLike.get();

            if(postLike.isLike_status()) {
                throw new IllegalArgumentException("이미 좋아요를 누른 게시물입니다.");
            }
            postLike.update(true);
        }

        if (optionalPostLike.isEmpty()) {
            PostLike postLike = new PostLike(user, post, true);
            postLikeRepository.save(postLike);
        }

    }

    // 게시물 좋아요 삭제
    @Transactional
    public void deleteLike(AuthUser authUser, Long postId) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물이 존재하지 않습니다.")
        );

        // 본인 게시물에 좋아요 취소를 누르는지 확인
        if (user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("본인 게시물에는 좋아요 취소를 누룰 수 없습니다.");
        }

        Optional<PostLike> optionalPostLike = postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        if(optionalPostLike.isPresent()) {
            PostLike postLike = optionalPostLike.get();

            if(!postLike.isLike_status()) {
                throw new IllegalArgumentException("좋아요를 누르지 않은 게시물입니다.");
            }

            postLike.update(false);
        }

    }
}
