package com.example.solutionchallenge.app.diary.domain;

import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.analysis.domain.StressLevel;
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
import jakarta.persistence.OneToOne;
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

    @Enumerated(EnumType.STRING)
    private DiaryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_activity_id")
    private RecommendedActivity recommendedActivity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stress_level_id")
    private StressLevel stressLevel;

    public void setUsers(Users users) {
        this.users = users;
        users.getDiaryList().add(this);
    }

    public void setRecommendedActivity(RecommendedActivity recommendedActivity) {
        this.recommendedActivity = recommendedActivity;
        recommendedActivity.getDiaryList().add(this);
    }

    public void setStressLevel(StressLevel stressLevel) {
        this.stressLevel = stressLevel;
    }

    @Builder
    public Diary(String content, String audioFileUrl, DiaryStatus status, Users users,
                 RecommendedActivity recommendedActivity, StressLevel stressLevel) {
        this.content = content;
        this.audioFileUrl = audioFileUrl;
        this.status = status;
        this.setUsers(users);
        this.setRecommendedActivity(recommendedActivity);
        this.setStressLevel(stressLevel);
    }
}
