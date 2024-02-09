package com.example.solutionchallenge.app.diary.service;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenProvider;
import com.example.solutionchallenge.app.diary.dto.response.DiaryResponseDto;
import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.dto.request.DiarySaveRequestDto;
import com.example.solutionchallenge.app.diary.repository.DiaryRepository;

import com.example.solutionchallenge.app.recommendedActivity.RecommendedActivityRepository;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UsersRepository usersRepository;
    private final RecommendedActivityRepository activityRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    public Long save(String accessToken, MultipartFile audioFile, DiarySaveRequestDto requestDto, Long activityId) {
        System.out.println("accessToken: " + accessToken);
        Users user = getUserByToken(accessToken);

        RecommendedActivity recommendedActivity = activityRepository.findById(activityId).orElseThrow(
                () -> new IllegalArgumentException("해당 추천 활동이 없습니다. id=" + activityId));

        try {
            ClassPathResource resource = new ClassPathResource("strecording-upload.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials)
                    .build().getService();

            String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
            String extension = audioFile.getContentType(); //파일 형식 flac

            BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, uuid).setContentType(extension).build(), audioFile.getBytes());
            String audioUrl = "https://storage.googleapis.com/" + bucketName + "/" + uuid;
            System.out.println(audioUrl);

            Long diaryId = diaryRepository.save(requestDto.toEntity(user, audioUrl, recommendedActivity)).getId();
            return diaryId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public DiaryResponseDto findById(String accessToken, Long diaryId) {
        getUserByToken(accessToken);
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(
                () -> new IllegalArgumentException("해당 일기가 없습니다. id=" + diaryId));
        DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
                .diary(diary).recommendedActivity(diary.getRecommendedActivity()).build();
        return diaryResponseDto;
    }

    public void deleteDiary(String accessToken, Long diaryId) {
        getUserByToken(accessToken);
        diaryRepository.deleteById(diaryId);
    }

    public Users getUserByToken(String accessToken) {
        Optional<Users> users = usersRepository.findById(jwtTokenProvider.extractSubjectFromJwt(accessToken));
        if (users.isPresent()) {
            return users.get();
        } else {
            throw new RuntimeException("토큰에 해당하는 멤버가 없습니다.");
        }
    }


}
