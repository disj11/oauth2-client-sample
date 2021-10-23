package com.example.oauth2.service;

import com.example.oauth2.service.bind.AccessTokenRequest;
import com.example.oauth2.service.bind.AccessTokenResponse;
import com.example.oauth2.service.bind.AuthorizationRequest;

public interface OAuth2Service {
    String getAuthorizationEndpoint(AuthorizationRequest request);
    AccessTokenResponse getAccessToken(AccessTokenRequest request);
}
