package com.example.newsfeed.domain.user.entity;

import com.example.newsfeed.common.entity.DateEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends DateEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true ,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String info;

    @Column(length = 4)
    private String mbti;

    public User(String name, String email, String password, String info, String mbti) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.info = info;
        this.mbti = mbti;
    }
}
