package com.example.solutionchallenge.domain.diary;

import com.example.solutionchallenge.domain.users.Users;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String audioFile;
    private String stressLevel;
    private String recommendedActivity;
    private DiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usersId")
    private Users users;

    public void setUsers(Users users) {
        this.users = users;
        users.getDiaryList().add(this);
    }

    @Builder
    public Diary(String content, String audioFile, String stressLevel, String recommendedActivity,
                 DiaryStatus status, Users users) {
        this.content = content;
        this.audioFile = audioFile;
        this.stressLevel = stressLevel;
        this.recommendedActivity = recommendedActivity;
        this.status = status;
        this.setUsers(users);
    }
}
