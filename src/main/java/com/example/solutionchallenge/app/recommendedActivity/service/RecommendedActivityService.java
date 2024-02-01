package com.example.solutionchallenge.app.recommendedActivity.service;

import com.example.solutionchallenge.app.recommendedActivity.RecommendedActivityRepository;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.recommendedActivity.dto.ActivityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class RecommendedActivityService {

    private final RecommendedActivityRepository recommendedActivityRepository;

    public ActivityResponseDto findById(Long activityId) {
        RecommendedActivity recommendedActivity = recommendedActivityRepository.findById(activityId).orElseThrow(
                () -> new IllegalArgumentException("해당 추천 활동이 없습니다. id=" + activityId));
        ActivityResponseDto activityResponseDto = ActivityResponseDto.builder().recommendedActivity(recommendedActivity).build();
        return activityResponseDto;
    }
}
