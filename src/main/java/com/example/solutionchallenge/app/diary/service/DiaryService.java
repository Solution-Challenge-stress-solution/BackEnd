package com.example.solutionchallenge.app.diary.service;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenProvider;
import com.example.solutionchallenge.app.diary.dto.response.DiaryResponseDto;
import com.example.solutionchallenge.app.diary.domain.Diary;
import com.example.solutionchallenge.app.diary.dto.request.DiarySaveRequestDto;
import com.example.solutionchallenge.app.diary.repository.DiaryRepository;

import com.example.solutionchallenge.app.recommendedActivity.RecommendedActivityRepository;
import com.example.solutionchallenge.app.recommendedActivity.domain.RecommendedActivity;
import com.example.solutionchallenge.app.stressLevel.domain.StressLevel;
import com.example.solutionchallenge.app.stressLevel.repository.StressLevelRepository;
import com.example.solutionchallenge.app.user.domain.Users;
import com.example.solutionchallenge.app.user.repository.UsersRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final StressLevelRepository stressLevelRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    public Long save(String accessToken, MultipartFile audioFile, DiarySaveRequestDto requestDto, Long activityId) {
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

            StressLevel stressLevel = StressLevel.builder()
                    .angry(requestDto.getAngry())
                    .sadness(requestDto.getSadness())
                    .disgusting(requestDto.getDisgusting())
                    .fear(requestDto.getFear())
                    .happiness(requestDto.getHappiness())
                    .stress_point(requestDto.getStress_point())
                    .max_emotion(requestDto.getMax_emotion())
                    .build();

            StressLevel stressEntity = stressLevelRepository.save(stressLevel);

            Long diaryId = diaryRepository.save(requestDto.toEntity(requestDto.getContent(), user, audioUrl, recommendedActivity, stressEntity)).getId();
            return diaryId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public DiaryResponseDto findByCreatedDate(String accessToken, String diaryDate) {
        Users user = getUserByToken(accessToken);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(diaryDate, formatter);

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = startDateTime.plusDays(1);

        Optional<Diary> diary = diaryRepository.findDiaryByUsersAndCreatedDateBetween(user, startDateTime, endDateTime);
        if (diary.isEmpty()) {
            throw new IllegalArgumentException("해당 일기가 없습니다. createdDate=" + date);
        }
        DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
                .diary(diary.get())
                .recommendedActivity(diary.get().getRecommendedActivity())
                .stressLevel(diary.get().getStressLevel())
                .build();
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
