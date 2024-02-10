package com.example.solutionchallenge.app.auth.domain.oauth2.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KakaoProfile {
    private Integer id;
    private LocalDateTime connectedAt;
    private String email;
    private String nickname;
    private String profileImage;

    public KakaoProfile(String jsonResponseBody){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonResponseBody);

        this.id = element.getAsJsonObject().get("id").getAsInt();

        String connected_at = element.getAsJsonObject().get("connected_at").getAsString();
        connected_at = connected_at.substring(0, connected_at.length() - 1);
        LocalDateTime connectDateTime = LocalDateTime.parse(connected_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.connectedAt = connectDateTime;

        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        this.nickname = properties.getAsJsonObject().get("nickname").getAsString();
        this.profileImage = properties.getAsJsonObject().get("profile_image").getAsString(); // 프로필 이미지 URL 추출

        JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
        this.email = kakaoAccount.getAsJsonObject().get("email").getAsString();
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
