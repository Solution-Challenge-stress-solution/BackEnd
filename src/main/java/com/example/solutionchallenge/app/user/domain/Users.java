package com.example.solutionchallenge.app.user.domain;

import com.example.solutionchallenge.app.diary.domain.BaseTimeEntity;
import com.example.solutionchallenge.app.diary.domain.Diary;
import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Diary> diaryList = new ArrayList<>();

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @Builder
    public Users(String name, String email, String profileImage, Gender gender, int age,
                 Status status, String accessToken, String refreshToken) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.gender = gender;
        this.age = age;
        this.status = status;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
