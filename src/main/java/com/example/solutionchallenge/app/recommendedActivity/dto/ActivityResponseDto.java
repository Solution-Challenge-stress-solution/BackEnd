package com.example.solutionchallenge.app.recommendedActivity.dto;

import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ActivityResponseDto {

    @Schema(description = "추천 활동 인덱스")
    private Long activityId;

    @Schema(description = "추천 활동명")
    private String title;

    @Schema(description = "추천 활동 이미지Url")
    private String imageUrl;

    @Schema(description = "추천 활동 내용")
    private String content;

    @Builder
    public ActivityResponseDto(RecommendedActivity recommendedActivity) {
        this.activityId = recommendedActivity.getId();
        this.title = recommendedActivity.getTitle();
        this.imageUrl = recommendedActivity.getImage();
        this.content = recommendedActivity.getContent();
    }
}
