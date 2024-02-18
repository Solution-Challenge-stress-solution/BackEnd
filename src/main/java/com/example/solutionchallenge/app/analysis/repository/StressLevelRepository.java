package com.example.solutionchallenge.app.analysis.repository;

import com.example.solutionchallenge.app.analysis.domain.StressLevel;
import com.example.solutionchallenge.app.diary.domain.Diary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StressLevelRepository extends JpaRepository<StressLevel, Long> {

    Optional<StressLevel> findByDiary(Diary diary);

}
