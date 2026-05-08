package com.skhu.jwtlogin.kakao.application;

import com.skhu.jwtlogin.kakao.api.dto.KakaoTokenResponse;
import com.skhu.jwtlogin.kakao.api.dto.KakaoUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAuthService {

    private final RestClient restClient = RestClient.create();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    public KakaoTokenResponse requestToken(String code) {
        return restClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        "grant_type=authorization_code"
                                + "&client_id=" + clientId
                                + "&client_secret=" + clientSecret
                                + "&redirect_uri=" + redirectUri
                                + "&code=" + code
                )
                .retrieve()
                .body(KakaoTokenResponse.class);
    }

    public KakaoUserInfoResponse requestUserInfo(String accessToken) {
        return restClient.get()
                .uri(userInfoUri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserInfoResponse.class);
    }
}