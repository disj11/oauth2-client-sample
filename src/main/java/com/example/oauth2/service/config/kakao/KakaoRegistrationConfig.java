package com.example.oauth2.service.config.kakao;

import com.example.oauth2.service.config.OAuth2RegistrationConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoRegistrationConfig implements OAuth2RegistrationConfig {
    @Value("${oauth2.kakao.registration.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.registration.client-secret}")
    private String clientSecret;

    @Value("${oauth2.kakao.registration.host}")
    private String host;


    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public String getHost() {
        return host;
    }
}
