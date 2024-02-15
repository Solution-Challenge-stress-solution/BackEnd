package com.example.solutionchallenge.app.emotion;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RestController
@RequestMapping("/emotion")
public class EmotionController {
    private static final Logger logger = LoggerFactory.getLogger(EmotionController.class);


    @Operation(summary = "감정 분석", description = "오디오파일 확장자는 .flac")
    @PostMapping(value = "/prediction", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> predict(@RequestParam("audioFile") MultipartFile audioFile) {
        logger.info("Received request to analyze emotion");

        byte[] audioBytes = null;
        try {
            audioBytes = audioFile.getBytes();
        } catch (IOException e) {
            logger.error("Error occurred while reading audio file: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        // 머신러닝 서버에 연결
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 65534)
                .usePlaintext()
                .build();

        // 머신러닝 서버에 gRPC 요청
        MyMLModelGrpc.MyMLModelBlockingStub stub = MyMLModelGrpc.newBlockingStub(channel);
        PredictRequest request = PredictRequest.newBuilder()
                .setAudio(ByteString.copyFrom(audioBytes))
                .build();

        PredictReply response = null;
        try {
            logger.info("Sending request to machine learning server");
            response = stub.predict(request);
            logger.info("Received response from machine learning server");
        } catch (Exception e) {
            logger.error("Error occurred: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            channel.shutdown();
        }

        // gRPC 응답을 Map으로 변환
        Map<String, String> resultMap = new HashMap<>();
        if (response != null) {
            for (Message.Emotion emotion : response.getEmotionsList()) {
                resultMap.put(emotion.getLabel(), emotion.getEmotionIndex());  // getProbabilityPercentage() 대신 getEmotionIndex() 사용
            }
            resultMap.put("max_emotion", response.getMaxEmotion().getLabel());
            resultMap.put("stress_index", response.getStressIndex());
        }

        return ResponseEntity.ok(resultMap);
    }
}





