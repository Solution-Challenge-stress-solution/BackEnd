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
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Diary> diaryList = new ArrayList<>();


    @Builder
    public Users(String name, String email, String profileImage) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.status = Status.JOIN;
    }


    public void update(String name, String email, String profileImage) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
