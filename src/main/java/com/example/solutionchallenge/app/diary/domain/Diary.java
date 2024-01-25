package com.example.solutionchallenge.app.diary.domain;

import com.example.solutionchallenge.app.user.domain.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

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
