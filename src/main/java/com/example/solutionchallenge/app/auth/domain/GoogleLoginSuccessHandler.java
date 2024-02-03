package com.example.solutionchallenge.app.auth.domain;

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
import jakarta.servlet.ServletException;

@RequiredArgsConstructor
@Component
public class GoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserMapper userMapper;
    private final OAuth2AuthorizedClientService authorizedClientService;

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
                userMapper.update(userDto);
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
