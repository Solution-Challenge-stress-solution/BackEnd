package com.example.solutionchallenge.domain.users;


import com.example.solutionchallenge.domain.BaseTimeEntity;
import com.example.solutionchallenge.domain.diary.Diary;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
