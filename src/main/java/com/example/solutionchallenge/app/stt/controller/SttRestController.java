package com.example.solutionchallenge.app.stt.controller;

import com.example.solutionchallenge.app.common.dto.response.ResponseDto;
import com.example.solutionchallenge.app.common.dto.response.ResponseUtil;
import com.example.solutionchallenge.app.stt.service.SpeechToTextService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseDto<String> handleAudioMessage(@RequestParam("audioFile") MultipartFile audioFile, @RequestParam("os") String os) throws IOException {
        int frequency = (os.equals("ios")) ? 48000 : 44100;
        String transcribe = sttService.transcribe(audioFile, frequency);
        return ResponseUtil.SUCCESS("변환에 성공하였습니다.", transcribe);
    }

}
