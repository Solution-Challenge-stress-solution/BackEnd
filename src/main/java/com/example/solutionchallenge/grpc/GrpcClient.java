package com.example.solutionchallenge.grpc;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import your_package.Message;
import your_package.MyMLModelGrpc;
import your_package.Message.PredictRequest;
import your_package.Message.PredictReply;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GrpcClient {
    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        String audioFilePath = "/path/to/your/audio/file"; //오디오파일 생성 절대 경로
        byte[] audioBytes = Files.readAllBytes(Paths.get(audioFilePath));

        MyMLModelGrpc.MyMLModelBlockingStub stub = MyMLModelGrpc.newBlockingStub(channel);
        PredictRequest request = PredictRequest.newBuilder()
                .setAudio(ByteString.copyFrom(audioBytes))
                .build();

        PredictReply response = stub.predict(request);

        // 응답에서 감정과 확률을 받아 출력합니다.
        for (Message.Emotion emotion : response.getEmotionsList()) {
            System.out.println("Label: " + emotion.getLabel() + ", Probability: " + emotion.getProbability());
        }

        channel.shutdown();
    }
}

