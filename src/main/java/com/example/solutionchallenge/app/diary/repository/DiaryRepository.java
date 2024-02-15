package com.example.solutionchallenge.app.diary.repository;

import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.user.domain.Users;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findDiaryByUsersAndCreatedDateBetween(Users users, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
