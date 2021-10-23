package com.example.oauth2.service;

import com.example.oauth2.service.config.OAuth2RegistrationConfig;
import com.example.oauth2.service.config.OAuth2ProviderConfig;
import com.example.oauth2.service.config.kakao.KakaoRegistrationConfig;
import com.example.oauth2.service.config.kakao.KakaoProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public enum OAuth2ServiceFactory {
    KAKAO("kakao", KakaoProviderConfig.class, KakaoRegistrationConfig.class);

    private final String providerName;
    private final Class<? extends OAuth2ProviderConfig> providerConfigClass;
    private final Class<? extends OAuth2RegistrationConfig> registrationConfigClass;

    private OAuth2ProviderConfig providerConfig;
    private OAuth2RegistrationConfig registrationConfig;

    OAuth2ServiceFactory(String providerName, Class<? extends OAuth2ProviderConfig> providerConfigClass, Class<? extends OAuth2RegistrationConfig> registrationConfigClass) {
        this.providerName = providerName;
        this.providerConfigClass = providerConfigClass;
        this.registrationConfigClass = registrationConfigClass;
    }

    public static OAuth2ServiceFactory of(String providerName) {
        for (OAuth2ServiceFactory provider : OAuth2ServiceFactory.values()) {
            if (provider.providerName.equals(providerName)) {
                return provider;
            }
        }

        throw new IllegalArgumentException("등록되지 않은 provider : " + providerName);
    }

    public static OAuth2Service createService(String providerName) {
        return of(providerName).createService();
    }

    public OAuth2Service createService() {
        return new OAuth2ServiceImpl(providerConfig, registrationConfig);
    }

    @Component
    public static class OAuth2ProviderInjector {
        private final ApplicationContext applicationContext;

        @Autowired
        public OAuth2ProviderInjector(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @PostConstruct
        public void postConstruct() {
            for (OAuth2ServiceFactory provider : OAuth2ServiceFactory.values()) {
                provider.providerConfig = applicationContext.getBean(provider.providerConfigClass);
                provider.registrationConfig = applicationContext.getBean(provider.registrationConfigClass);
            }
        }
    }
}
