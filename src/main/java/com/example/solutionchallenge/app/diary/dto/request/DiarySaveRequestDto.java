package com.example.solutionchallenge.app.diary.dto.request;

import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.domain.DiaryStatus;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.stressLevel.domain.StressLevel;
import com.example.solutionchallenge.app.user.domain.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "일기 저장 요청")
@Getter
@Setter
@NoArgsConstructor
public class DiarySaveRequestDto {

    @Schema(description = "일기 내용", example = "오늘 하루도 힘들었다.~~")
    private String content;
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

    public Diary toEntity(String content, Users users, String audioFileUrl, RecommendedActivity recommendedActivity, StressLevel stressLevel) {
        return Diary.builder()
                .content(content)
                .audioFileUrl(audioFileUrl)
                .recommendedActivity(recommendedActivity)
                .stressLevel(stressLevel)
                .status(DiaryStatus.CREATED)
                .users(users)
                .build();
    }




}
