package com.example.solutionchallenge.app.diary.dto.response;

import com.example.solutionchallenge.app.diary.entity.Diary;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryResponseDto {

    private Long diaryId;

    private String content;

    private String audioFileUrl;

    private String stressLevel;

    private String recommendedActivity;

    @Builder
    public DiaryResponseDto(Diary diary) {
        this.diaryId = diary.getId();
        this.content = diary.getContent();
        this.audioFileUrl = diary.getAudioFileUrl();
        this.stressLevel = diary.getStressLevel();
        this.recommendedActivity = diary.getRecommendedActivity();
    }
}
