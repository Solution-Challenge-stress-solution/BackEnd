package com.example.solutionchallenge.app.diary.dto.response;

import com.example.solutionchallenge.app.analysis.domain.StressLevel;
import com.example.solutionchallenge.app.diary.domain.Diary;
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

    @Schema(description = "분노 지수")
    private double angry;

    @Schema(description = "슬픔 지수")
    private double sadness;

    @Schema(description = "역겨움 지수")
    private double disgusting;

    @Schema(description = "두려움 지수")
    private double fear;

    @Schema(description = "행복 지수")
    private double happiness;

    @Schema(description = "종합 스트레스 지수")
    private double stress_point;

    @Schema(description = "최고 감정")
    private String max_emotion;

    @Schema(description = "추천 활동 인덱스")
    private Long activityId;

    @Builder
    public DiaryResponseDto(Diary diary, StressLevel stressLevel, Long activityId) {
        this.diaryId = diary.getId();
        this.content = diary.getContent();
        this.audioFileUrl = diary.getAudioFileUrl();
        this.angry = stressLevel.getAngry();
        this.sadness = stressLevel.getSadness();
        this.disgusting = stressLevel.getDisgusting();
        this.fear = stressLevel.getFear();
        this.happiness = stressLevel.getHappiness();
        this.stress_point = stressLevel.getStress_point();
        this.max_emotion = stressLevel.getMax_emotion();
        this.activityId = activityId;
    }
}
