package com.example.solutionchallenge.app.analysis.repository;

import com.example.solutionchallenge.app.analysis.domain.StressLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StressLevelRepository extends JpaRepository<StressLevel, Long> {
}
