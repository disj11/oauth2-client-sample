package com.example.oauth2.service.bind;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AccessTokenRequest {
    private final String grantType;
    private final String code;

    public AccessTokenRequest(String code) {
        this.grantType = "authorization_code";
        this.code = code;
    }
}
