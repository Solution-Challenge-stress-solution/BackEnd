package com.example.solutionchallenge.app.diary.entity;

import com.example.solutionchallenge.app.common.entity.BaseTimeEntity;
import com.example.solutionchallenge.app.user.entity.Users;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String audioFileUrl;
    private String stressLevel;
    private String recommendedActivity;

    @Enumerated(EnumType.STRING)
    private DiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public void setUsers(Users users) {
        this.users = users;
        users.getDiaryList().add(this);
    }

    @Builder
    public Diary(String content, String audioFileUrl, String stressLevel, String recommendedActivity,
                 DiaryStatus status, Users users) {
        this.content = content;
        this.audioFileUrl = audioFileUrl;
        this.stressLevel = stressLevel;
        this.recommendedActivity = recommendedActivity;
        this.status = status;
        this.setUsers(users);
    }
}
