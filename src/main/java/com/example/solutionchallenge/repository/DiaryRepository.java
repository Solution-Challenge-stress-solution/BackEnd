package com.example.solutionchallenge.repository;

import com.example.solutionchallenge.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
