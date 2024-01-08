package com.example.solutionchallenge.common.oauth.provider;
public interface OAuthUserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
