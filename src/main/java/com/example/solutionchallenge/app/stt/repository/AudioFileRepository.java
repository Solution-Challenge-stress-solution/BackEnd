package com.example.solutionchallenge.app.stt.repository;

import com.example.solutionchallenge.app.stt.domain.AudioFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
}