package com.example.solutionchallenge.app.analysis.controller;

import com.example.solutionchallenge.app.analysis.service.AnalysisService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import your_package.Message;
import your_package.MyMLModelGrpc;
import your_package.Message.PredictRequest;
import your_package.Message.PredictReply;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "감정 분석", description = "감정 분석 API")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "API 정상 작동")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/emotion")
public class AnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private final AnalysisService analysisService;

    @Operation(summary = "감정 분석", description = "오디오파일 확장자는 .flac")
    @PostMapping(value = "/prediction/diaries/{diaryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> predict(@PathVariable("diaryId") Long diaryId, @RequestParam("audioFile") MultipartFile audioFile) {
        logger.info("Received request to analyze emotion");
        return ResponseEntity.ok(analysisService.predict(audioFile, diaryId));
    }
}





