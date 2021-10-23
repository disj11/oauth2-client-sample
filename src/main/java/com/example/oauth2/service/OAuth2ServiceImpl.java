package com.example.oauth2.service;

import com.example.oauth2.service.bind.AccessTokenRequest;
import com.example.oauth2.service.bind.AccessTokenResponse;
import com.example.oauth2.service.bind.AuthorizationRequest;
import com.example.oauth2.service.config.OAuth2ProviderConfig;
import com.example.oauth2.service.config.OAuth2RegistrationConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class OAuth2ServiceImpl implements OAuth2Service {
    private final RestTemplate restTemplate;
    private final OAuth2ProviderConfig providerConfig;
    private final OAuth2RegistrationConfig registrationConfig;

    public OAuth2ServiceImpl(OAuth2ProviderConfig providerConfig, OAuth2RegistrationConfig registrationConfig) {
        this.restTemplate = new RestTemplate();
        this.providerConfig = providerConfig;
        this.registrationConfig = registrationConfig;
    }

    @Override
    public String getAuthorizationEndpoint(AuthorizationRequest request) {
        return providerConfig.getAuthorizationEndpoint()
                + "?client_id=" + Objects.requireNonNull(registrationConfig.getClientId())
                + "&redirect_uri=" + getRedirectUri()
                + "&" + request.queryString();
    }

    @Override
    public AccessTokenResponse getAccessToken(AccessTokenRequest request) {
        ResponseEntity<Map<String, Object>> responseMap = post(
                providerConfig.getTokenEndpoint(),
                new HttpEntity<>(getTokenRequestData(request), getTokenRequestHeaders())
        );

        if (!responseMap.getStatusCode().equals(HttpStatus.OK)) {
            throw new OAuth2Exception(getErrorMessage(responseMap));
        }

        return getTokenResponse(responseMap);
    }

    private ResponseEntity<Map<String, Object>> post(String uri, HttpEntity<?> requestEntity) {
        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    private String getRedirectUri() {
        return registrationConfig.getHost() + "/oauth/" + providerConfig.getProviderName() + "/authorization";
    }

    private String getErrorMessage(ResponseEntity<Map<String, Object>> responseMap) {
        Map<String, Object> body = Objects.requireNonNull(responseMap.getBody());
        String error = Objects.requireNonNull((String) body.get("error"));
        String errorDescription = (String) body.get("error_description");
        String errorUri = (String) body.get("error_uri");

        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append("code: ").append(responseMap.getStatusCode());
        errorMessageBuilder.append(", error: ").append(error);
        Optional.ofNullable(errorDescription).ifPresent(value -> errorMessageBuilder.append(", error_description: ").append(value));
        Optional.ofNullable(errorUri).ifPresent(value -> errorMessageBuilder.append(", error_uri: ").append(value));

        return errorMessageBuilder.toString();
    }

    private HttpHeaders getTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> getTokenRequestData(AccessTokenRequest request) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", Objects.requireNonNull(request.getGrantType()));
        map.add("client_id", Objects.requireNonNull(registrationConfig.getClientId()));
        map.add("code", Objects.requireNonNull(request.getCode()));
        map.add("redirect_uri", getRedirectUri());
        if (registrationConfig.getClientSecret() != null && !"".equals(registrationConfig.getClientSecret())) {
            map.add("client_secret", registrationConfig.getClientSecret());
        }
        return map;
    }

    private AccessTokenResponse getTokenResponse(ResponseEntity<Map<String, Object>> responseMap) {
        Map<String, Object> body = Objects.requireNonNull(responseMap.getBody());
        String accessToken = (String) Objects.requireNonNull(body.get("access_token"));
        String tokenType = (String) Objects.requireNonNull(body.get("token_type"));
        Integer expiresIn = (Integer) body.get("expires_in");
        String refreshToken = (String) body.get("refresh_token");
        String scope = (String) body.get("scope");

        return AccessTokenResponse.builder(accessToken, tokenType)
                .expiresIn(expiresIn)
                .refreshToken(refreshToken)
                .scope(scope)
                .build();
    }
}
