package com.example.oauth2.service.bind;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;
import java.util.StringJoiner;

@Getter
@ToString
@Builder
public class AuthorizationRequest {
    private final String scope;
    private final String state;

    public String queryString() {
        StringJoiner joiner = new StringJoiner("&");
        joiner.add("response_type=code");
        Optional.ofNullable(scope).ifPresent(scope -> joiner.add("scope=" + scope));
        Optional.ofNullable(state).ifPresent(state -> joiner.add("state=" + state));
        return joiner.toString();
    }
}
