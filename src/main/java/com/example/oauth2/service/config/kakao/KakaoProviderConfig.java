package com.example.oauth2.service.config.kakao;

import com.example.oauth2.service.config.OAuth2ProviderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoProviderConfig implements OAuth2ProviderConfig {
    @Value("${oauth2.kakao.provider.name}")
    private String providerName;

    @Value("${oauth2.kakao.provider.authorization-endpoint}")
    private String authorizationEndpoint;

    @Value("${oauth2.kakao.provider.token-endpoint}")
    private String tokenEndpoint;

    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    @Override
    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
}
