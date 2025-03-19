package com.example.newsfeed.domain.user.repository;

import com.example.newsfeed.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 이메일로_사용자를_조회할_수_있다() {
        // given
        String email = "aaa@aaa.com";
        User user = new User("홍길동", email, "Asdf1234!", "자기소개", "isfp");
        ReflectionTestUtils.setField(user, "id", 1L);
        userRepository.save(user);

        // when
        //Optional<User> optionalUser = userRepository.findByEmail(email);
        User foundUser = userRepository.findByEmail(email).orElse(null);

        // then
//        assertThat(optionalUser.isPresent()).isTrue();
//        assertThat(email).isEqualTo(optionalUser.get().getEmail());

        assertNotNull(foundUser);
    }

}