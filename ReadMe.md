## OAuth 2.0 클라이언트 샘플

네아로, 카카오 로그인 등 OAuth 2.0 방식의 로그인 연동 샘플 프로젝트이다. OAuth 2.0 규칙을 최대한 지켜 대부분의 서비스에서 변경 없이 사용할 수 있도록 하였다. OAuth 2.0에 대해 궁금하다면 [이 포스팅](https://disj11.github.io/development/rfc6749-oauth2/)을 참고한다. 

## interface 구현

어떤 로그인 서비스를 연동할 것인지 `com.example.oauth2.service.config.OAuth2ProviderConfig`와 `com.example.oauth2.service.config.kakao.KakaoProviderConfig` 인터페이스를 통하여 설정하도록 하였다. 두 인터페이스의 구현체는 스프링의 `ApplicationContext`에 등록되어야 한다. `com.example.oauth2.service.config.kakao.KakaoRegistrationConfig`와 `com.example.oauth2.service.config.kakao.KakaoProviderConfig`에 샘플을 만들어 놓았다.

## provider 설정

config interface를 구현하였으면, `com.example.oauth2.service.OAuth2ServiceFactory`에 해당 설정파일을 등록한다. 이 프로젝트에서는 카카오를 테스트 샘플로 등록해놓았다.

## application properties

샘플로 만들어 놓은 카카오 로그인을 테스트해보기 위해서는 프로퍼티를 등록해야한다.
등록해야하는 프로퍼티는 다음과 같다. 

`application.properties`:

```properties
oauth2.kakao.provider.name=kakao
oauth2.kakao.provider.authorization-endpoint=https://kauth.kakao.com/oauth/authorize
oauth2.kakao.provider.token-endpoint=https://kauth.kakao.com/oauth/token

oauth2.kakao.registration.client-id={REST API KEY}
oauth2.kakao.registration.client-secret={Client Secret}
oauth2.kakao.registration.host=http://localhost:8080
```

## 토큰 발급

토큰 발급이 완료되면, 발급된 토큰 정보를 `{host}/oauth/{providerName}/success`로 전달하도록 구현하였다. 로그인 서비스가 추가되면 이 엔드포인트를 만들어 추가적인 로직을 구현한다. `com.example.oauth2.kakao.KakaoLoginController` 파일 참고.

## 로그인

로그인 버튼을 누르면 `/oauth/{providerName}/authorize`를 오픈하도록 한다.

## 테스트

[application.properties](#application-properties)만 설정하면 다른 설정없이 카카오 로그인 API를 테스트할 수 있도록 하였다. `oauth2.kakao.registration.client-id` 속성에 REST API 키를, `oauth2.kakao.registration.client-secret` 속성에 Client Secret을 입력한다. 만약 보안 설정을 활성화 하지 않았다면 Client Secret 프로퍼티는 비워둔다. 설정을 완료하였으면, `http://localhost:8080` 에서 카카오 로그인 버튼을 눌러 테스트해본다.