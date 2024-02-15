package com.example.solutionchallenge.app.emotion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import your_package.Message;
import your_package.MyMLModelGrpc;
import your_package.Message.PredictRequest;
import your_package.Message.PredictReply;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmotionController {
    private static final Logger logger = LoggerFactory.getLogger(EmotionController.class);

    @PostMapping("/predict")
    public ResponseEntity<Map<String, Double>> predict(@RequestBody byte[] audioBytes) {
        logger.info("Received request to analyze emotion");

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
        Map<String, Double> resultMap = new HashMap<>();
        if (response != null) {
            for (Message.Emotion emotion : response.getEmotionsList()) {
                resultMap.put(emotion.getLabel(), Double.valueOf(emotion.getProbability()));
            }
        }

        return ResponseEntity.ok(resultMap);
    }
}


