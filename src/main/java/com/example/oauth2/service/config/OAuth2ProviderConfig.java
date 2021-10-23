package com.example.oauth2.service.config;

public interface OAuth2ProviderConfig {
    String getProviderName();
    String getAuthorizationEndpoint();
    String getTokenEndpoint();
}
