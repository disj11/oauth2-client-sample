package com.example.oauth2.ui;

import com.example.oauth2.service.OAuth2Service;
import com.example.oauth2.service.OAuth2ServiceFactory;
import com.example.oauth2.service.bind.AccessTokenRequest;
import com.example.oauth2.service.bind.AccessTokenResponse;
import com.example.oauth2.service.bind.AuthorizationRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/oauth")
public class OAuth2Controller {
    @GetMapping("{providerName}/authorize")
    public ResponseEntity<String> getAuthorizationUri(
            @PathVariable String providerName,
            @RequestParam(required = false) String scope
    ) {
        OAuth2Service oAuth2Service = OAuth2ServiceFactory.createService(providerName);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, oAuth2Service.getAuthorizationEndpoint(
                        AuthorizationRequest.builder()
                                .scope(scope)
                                .state(UUID.randomUUID().toString())
                                .build()
                ))
                .build();
    }

    @GetMapping("{providerName}/authorization")
    public ResponseEntity<String> getNewToken(
            @PathVariable String providerName,
            @RequestParam String code,
            @RequestParam(required = false) String state
    ) {
        // state 검증하여 CSRF 방어 필요

        OAuth2Service service = OAuth2ServiceFactory.createService(providerName);
        AccessTokenResponse response = service.getAccessToken(new AccessTokenRequest(code));
        String successUri = "/oauth/" + providerName + "/success?" + response.queryString();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, successUri)
                .build();
    }
}
