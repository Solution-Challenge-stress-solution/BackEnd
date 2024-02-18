package com.example.solutionchallenge.app.analysis.domain;

import com.example.solutionchallenge.app.diary.domain.Diary;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class StressLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double angry;
    private double sadness;
    private double disgusting;
    private double fear;
    private double happiness;
    private double stress_point;
    private String max_emotion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    @Builder
    public StressLevel(double angry, double sadness, double disgusting, double fear, double happiness,
                       double stress_point,
                       String max_emotion, Diary diary) {
        this.angry = angry;
        this.sadness = sadness;
        this.disgusting = disgusting;
        this.fear = fear;
        this.happiness = happiness;
        this.stress_point = stress_point;
        this.max_emotion = max_emotion;
        setDiary(diary);
    }
}
