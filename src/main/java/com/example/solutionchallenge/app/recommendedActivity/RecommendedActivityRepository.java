package com.example.solutionchallenge.app.recommendedActivity;

import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendedActivityRepository extends JpaRepository<RecommendedActivity, Long> {
}
