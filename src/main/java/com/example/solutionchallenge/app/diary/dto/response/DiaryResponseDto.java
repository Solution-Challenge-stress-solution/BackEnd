package com.example.solutionchallenge.app.diary.dto.response;

import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryResponseDto {

    @Schema(description = "일기 인덱스")
    private Long diaryId;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "오디오 파일 링크")
    private String audioFileUrl;

    @Schema(description = "스트레스 지수")
    private String stressLevel;

    @Schema(description = "추천 활동 이미지 url")
    private String activityImageUrl;

    @Schema(description = "추천 활동 이름")
    private String activityTitle;

    @Schema(description = "추천 활동")
    private String activityContent;

    @Builder
    public DiaryResponseDto(Diary diary, RecommendedActivity recommendedActivity) {
        this.diaryId = diary.getId();
        this.content = diary.getContent();
        this.audioFileUrl = diary.getAudioFileUrl();
        this.stressLevel = diary.getStressLevel();
        this.activityImageUrl = recommendedActivity.getImage();
        this.activityTitle = recommendedActivity.getTitle();
        this.activityContent = recommendedActivity.getContent();
    }
}
