package com.example.solutionchallenge.app.diary.repository;

import com.example.solutionchallenge.app.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
