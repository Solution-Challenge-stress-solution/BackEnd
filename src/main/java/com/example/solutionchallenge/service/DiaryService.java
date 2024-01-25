package com.example.solutionchallenge.service;

import com.example.solutionchallenge.domain.diary.Diary;
import com.example.solutionchallenge.domain.users.Users;
import com.example.solutionchallenge.dto.diary.DiaryResponseDto;
import com.example.solutionchallenge.dto.diary.DiarySaveRequestDto;
import com.example.solutionchallenge.repository.DiaryRepository;
import com.example.solutionchallenge.repository.UsersRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;

    private final DiaryRepository diaryRepository;
    private final UsersRepository usersRepository;

    public Long save(MultipartFile audioFile, DiarySaveRequestDto requestDto, Long userId) {
        Users users = usersRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        try {
            ClassPathResource resource = new ClassPathResource("storage-access.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials)
                    .build().getService();

            String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
            String extension = audioFile.getContentType(); //파일 형식 flac

            BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, uuid).setContentType(extension).build(), audioFile.getBytes());
            String audioUrl = "https://storage.googleapis.com/" + bucketName + "/" + uuid;
            System.out.println(audioUrl);

            Long diaryId = diaryRepository.save(requestDto.toEntity(users, audioUrl)).getId();
            return diaryId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public DiaryResponseDto findById(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(
                () -> new IllegalArgumentException("해당 일기가 없습니다. id=" + diaryId));
        DiaryResponseDto diaryResponseDto = DiaryResponseDto.builder()
                .diary(diary).build();
        return diaryResponseDto;
    }

    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }

}
