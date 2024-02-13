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

    public ActivityResponseDto findRandom() {
        double dValue = Math.random();
        long randomId = (long) ((dValue * 20) + 1);
        RecommendedActivity recommendedActivity = recommendedActivityRepository.findById(randomId).orElseThrow(
                () -> new IllegalArgumentException("해당 추천 활동이 없습니다. id=" + randomId));
        ActivityResponseDto activityResponseDto = ActivityResponseDto.builder().recommendedActivity(recommendedActivity).build();
        return activityResponseDto;
    }
}
