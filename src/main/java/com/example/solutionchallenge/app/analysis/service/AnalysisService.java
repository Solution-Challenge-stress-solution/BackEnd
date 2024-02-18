package com.example.solutionchallenge.app.analysis.service;

import com.example.solutionchallenge.app.analysis.controller.AnalysisController;
import com.example.solutionchallenge.app.analysis.domain.StressLevel;
import com.example.solutionchallenge.app.analysis.repository.StressLevelRepository;
import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.repository.DiaryRepository;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import your_package.Message;
import your_package.Message.PredictReply;
import your_package.Message.PredictRequest;
import your_package.MyMLModelGrpc;

@RequiredArgsConstructor
@Transactional
@Service
public class AnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private final DiaryRepository diaryRepository;
    private final StressLevelRepository stressLevelRepository;


    public Map<String, String> predict(MultipartFile audioFile, Long diaryId) {

        byte[] audioBytes = null;
        try {
            audioBytes = audioFile.getBytes();
        } catch (IOException e) {
            logger.error("Error occurred while reading audio file: ", e);
            throw new RuntimeException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        // 머신러닝 서버에 연결
        ManagedChannel channel = ManagedChannelBuilder.forAddress("34.64.90.112", 12345)
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
            throw new RuntimeException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
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

        Optional<Diary> diary = diaryRepository.findById(diaryId);

        double angry = 0;
        double sadness = 0;
        double disgusting = 0;
        double fear = 0;
        double happiness = 0;
        double stress_point = 0;
        String max_emotion = "";

        for (String emotion : resultMap.keySet()) {
            switch (emotion) {
                case "h":
                    happiness = Double.parseDouble(resultMap.get(emotion));
                    break;
                case "a":
                    angry = Double.parseDouble(resultMap.get(emotion));
                    break;
                case "s":
                    sadness = Double.parseDouble(resultMap.get(emotion));
                    break;
                case "d":
                    disgusting = Double.parseDouble(resultMap.get(emotion));
                    break;
                case "f":
                    fear = Double.parseDouble(resultMap.get(emotion));
                    break;
                case "stress_index":
                    stress_point = Double.parseDouble(resultMap.get(emotion));
                    break;
                case "max_emotion":
                    max_emotion = resultMap.get(emotion);
                    break;
            }
        }

        StressLevel stressLevel = StressLevel.builder()
                .angry(angry)
                .sadness(sadness)
                .disgusting(disgusting)
                .fear(fear)
                .happiness(happiness)
                .stress_point(stress_point)
                .max_emotion(max_emotion)
                .diary(diary.get())
                .build();

        stressLevelRepository.save(stressLevel);
        return resultMap;
    }

}
