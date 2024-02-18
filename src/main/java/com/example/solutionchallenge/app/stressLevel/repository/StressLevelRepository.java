package com.example.solutionchallenge.app.stressLevel.repository;

import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.stressLevel.domain.StressLevel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StressLevelRepository extends JpaRepository<StressLevel, Long> {

    Optional<StressLevel> findByDiary(Diary diary);

}
