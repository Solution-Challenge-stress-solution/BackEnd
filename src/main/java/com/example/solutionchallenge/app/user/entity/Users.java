package com.example.solutionchallenge.app.user.entity;


import com.example.solutionchallenge.app.common.entity.BaseTimeEntity;
import com.example.solutionchallenge.app.diary.domain.Diary;
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

    private String firebaseUid;

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

    public void changeName(String name) {
        this.name = name;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }

    public void changeAge(int age) {
        this.age = age;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

}
