package com.example.solutionchallenge.app.stt.controller;

import com.example.solutionchallenge.app.stt.service.SpeechToTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/stt")
public class SttRestController {

    @Autowired
    private SpeechToTextService sttService;
    /**
     * 녹음 파일을 받아서 텍스트로 변환하여 반환
     *
     * @param audioFile 오디오 파일
     * @return 녹음 파일을 변환한 텍스트
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleAudioMessage(@RequestParam("audioFile") MultipartFile audioFile, @RequestParam("os") String os, @RequestParam("lang") String lang) throws IOException {
        int frequency = (os.equals("ios")) ? 48000 : 44100;
        String language = (lang.equals("ko")) ? "ko-kR" : "en-US";
        String transcribe = sttService.transcribe(audioFile, frequency, language);
        return ResponseEntity.ok().body(transcribe);
    }

}
