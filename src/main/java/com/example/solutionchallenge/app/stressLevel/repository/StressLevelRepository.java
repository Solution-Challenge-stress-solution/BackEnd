package com.example.solutionchallenge.app.stressLevel.repository;

import com.example.solutionchallenge.app.stressLevel.domain.StressLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StressLevelRepository extends JpaRepository<StressLevel, Long> {
}
