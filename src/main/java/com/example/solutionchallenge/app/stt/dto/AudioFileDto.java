package com.example.solutionchallenge.app.stt.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AudioFileDto {
    private MultipartFile audioFile;
    private String os;
    private String lang;
}
