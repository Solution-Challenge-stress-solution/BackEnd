package com.example.solutionchallenge.app.diary.dto.request;

import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.domain.DiaryStatus;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.analysis.domain.StressLevel;
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

    public Diary toEntity(String content, Users users, String audioFileUrl) {
        return Diary.builder()
                .content(content)
                .audioFileUrl(audioFileUrl)
                .status(DiaryStatus.CREATED)
                .users(users)
                .build();
    }




}
