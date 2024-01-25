package com.example.solutionchallenge.app.auth.domain.jwt;

import com.example.solutionchallenge.app.common.exception.ApiException;
import com.example.solutionchallenge.app.common.constant.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Random;

@Service
public class JwtTokenService implements InitializingBean {
    private long accessTokenExpirationInSeconds;
    private long refreshTokenExpirationInSeconds;
    private final String secretKey;
    private Key key;

    public JwtTokenService(
            @Value("${spring.security.jwt.token.access-token-expiration-seconds}") long accessTokenExpirationInSeconds,
            @Value("${spring.security.jwt.token.refresh-token-expiration-seconds}") long refreshTokenExpirationInSeconds,
            @Value("${spring.security.jwt.token.secret-key}") String secretKey
    ) {
        this.accessTokenExpirationInSeconds = accessTokenExpirationInSeconds * 1000;
        this.refreshTokenExpirationInSeconds = refreshTokenExpirationInSeconds * 1000;
        this.secretKey = secretKey;
    }

    // 빈 주입받은 후 실행되는 메소드

    @Override
    public void afterPropertiesSet() {
        System.out.println("Secret key: " + secretKey);
        String base64EncodedSecretKey = encodeBase64SecretKey(secretKey);
        System.out.println("Base64 encoded secret key: " + base64EncodedSecretKey);
        this.key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
        System.out.println("Key: " + key);
    }
    public String createAccessToken(String payload){
        return createToken(payload, accessTokenExpirationInSeconds);
    }

    public String createRefreshToken(){
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        return createToken(generatedString, refreshTokenExpirationInSeconds);
    }

    public String createToken(String payload, long expireLength){
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPayload(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        }catch (JwtException e){
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }
    }

    public boolean validateToken(String token){
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException exception){
            return false;
        }
    }

    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    //클라이언트 쿠키에 리프레시토큰 저장 시켜주는 메소드
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Long age = refreshTokenExpirationInSeconds;
        Cookie cookie = new Cookie("refresh_token",refreshToken);
        cookie.setPath("/");
        cookie.setMaxAge(age.intValue());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
