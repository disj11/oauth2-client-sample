package com.example.oauth2.service.bind;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;
import java.util.StringJoiner;

@Getter
@ToString
@Builder
public class AccessTokenResponse {
    private final String accessToken;
    private final String tokenType;

    private final Integer expiresIn;
    private final String refreshToken;
    private final String scope;

    public String queryString() {
        StringJoiner joiner = new StringJoiner("&");
        joiner.add("access_token=" + accessToken);
        joiner.add("token_type=" + tokenType);
        Optional.ofNullable(expiresIn).ifPresent(expiresIn -> joiner.add("expires_in=" + expiresIn));
        Optional.ofNullable(refreshToken).ifPresent(refreshToken -> joiner.add("refresh_token=" + refreshToken));
        Optional.ofNullable(scope).ifPresent(scope -> joiner.add("scope=" + scope));
        return joiner.toString();
    }

    private static AccessTokenResponseBuilder builder() {
        return new AccessTokenResponseBuilder();
    }

    public static AccessTokenResponseBuilder builder(String accessToken, String tokenType) {
        return builder()
                .accessToken(accessToken)
                .tokenType(tokenType);
    }
}
