package com.example.newsfeed.domain.post.repository;

import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

//    @Test
//    void 생성일순으로_게시물을_조회할_수_있다() {
//
//        String email = "aaa@aaa.com";
//        User user = new User("홍길동", "aaa@aaa.com", "Asdf1234!", "자기소개", "isfp");
//
//        Post post1 = new Post("제목1", "내용1", user);
//        Post post2 = new Post("제목2", "내용2", user);
//        Post post3 = new Post("제목3", "내용3", user);
//
//        int page = 1;
//        int size = 10;
//        Pageable pageable = PageRequest.of(page,size);
//
//        Page<Post> postPage = postRepository.findAllByCreatedAt(pageable);
//
//        assertThat(postPage.getContent().equals(post1)).isTrue();
//
//
//    }

}