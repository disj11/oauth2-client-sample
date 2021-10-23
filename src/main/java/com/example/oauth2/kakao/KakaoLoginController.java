package com.example.oauth2.kakao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoLoginController {
    @GetMapping("/success")
    public Map<String, String> generatedToken(@RequestParam Map<String, String> request) {
        // request 데이트는 AccessTokenResponse 참고
        // request: {"access_token":"","token_type":"bearer","expires_in":"21599","refresh_token":""}
        return request;
    }
}
