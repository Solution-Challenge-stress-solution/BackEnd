package com.example.solutionchallenge.app.stt.service;

import com.example.solutionchallenge.app.stt.repository.AudioFileRepository;
import org.springframework.stereotype.Service;

@Service
public class AudioFileService {
    private final AudioFileRepository audioFileRepository;

    public AudioFileService(AudioFileRepository audioFileRepository) {
        this.audioFileRepository = audioFileRepository;
    }

    public void deleteAudioFile(Long id) {
        audioFileRepository.deleteById(id);
    }
}

