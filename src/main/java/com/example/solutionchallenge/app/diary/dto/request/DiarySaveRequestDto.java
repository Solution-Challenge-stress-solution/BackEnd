package com.example.solutionchallenge.app.diary.dto.request;

import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.domain.DiaryStatus;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
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
//    @Schema(name = "오디오 파일(.flac)", example = "todayDiary.flac")
//    private MultipartFile audioFile;
    @Schema(description = "스트레스 지수", example = "상, 중, 하")
    private String stressLevel;
    @Schema(description = "추천 활동", example = "걷기")
    private String recommendedActivity;

    public Diary toEntity(Users users, String audioFileUrl, RecommendedActivity recommendedActivity) {
        return Diary.builder()
                .content(content)
                .audioFileUrl(audioFileUrl)
                .stressLevel(stressLevel)
                .recommendedActivity(recommendedActivity)
                .status(DiaryStatus.CREATED)
                .users(users)
                .build();
    }


}
