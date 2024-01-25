package com.example.solutionchallenge.app.user.domain;


import com.example.solutionchallenge.app.diary.domain.BaseTimeEntity;
import com.example.solutionchallenge.app.diary.domain.Diary;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

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

    @Builder
    public Users(String name, String email, String profileImage, Gender gender, int age,
                 Status status) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.gender = gender;
        this.age = age;
        this.status = status;
    }
}