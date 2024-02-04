package com.example.solutionchallenge.app.auth.handler;

import com.example.solutionchallenge.app.auth.domain.jwt.JwtProperties;
import com.example.solutionchallenge.app.auth.domain.jwt.JwtTokenProvider;
import com.example.solutionchallenge.app.user.dto.UserDto;
import com.example.solutionchallenge.app.user.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import jakarta.servlet.ServletException;

@RequiredArgsConstructor
@Component
public class GoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserMapper userMapper;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

        if ("google".equals(oauthToken.getAuthorizedClientRegistrationId())) {
            String email = oauthToken.getPrincipal().getAttributes().get("email").toString();
            UserDto userDto = userMapper.findByEmail(email);
            if (userDto != null) {
                userDto.setAccessToken(authorizedClient.getAccessToken().getTokenValue());
                userDto.setRefreshToken(authorizedClient.getRefreshToken().getTokenValue());

                // JWT를 생성하고 이를 HTTP 응답에 포함시킴
                Date expiry = new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationSeconds() * 1000);
                String jwt = jwtTokenProvider.createToken(expiry, userDto);
                response.addHeader("Authorization", "Bearer " + jwt);
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
