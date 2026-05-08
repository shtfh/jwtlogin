package com.skhu.jwtlogin.kakao.application;

import com.skhu.jwtlogin.global.jwt.JwtProvider;
import com.skhu.jwtlogin.kakao.api.dto.KakaoLoginResponse;
import com.skhu.jwtlogin.kakao.api.dto.KakaoTokenResponse;
import com.skhu.jwtlogin.kakao.api.dto.KakaoUserInfoResponse;
import com.skhu.jwtlogin.member.domain.Member;
import com.skhu.jwtlogin.member.domain.Role;
import com.skhu.jwtlogin.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

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

    public KakaoLoginResponse login(String code) {
        KakaoTokenResponse tokenResponse = requestToken(code);
        KakaoUserInfoResponse userInfoResponse = requestUserInfo(tokenResponse.accessToken());

        Member member = memberRepository.findByKakaoId(userInfoResponse.id())
                .orElseGet(() -> saveMember(userInfoResponse));

        String accessToken = jwtProvider.createAccessToken(member.getMemberId());

        return new KakaoLoginResponse(
                member.getMemberId(),
                accessToken,
                member.getNickname()
        );
    }

    private Member saveMember(KakaoUserInfoResponse userInfoResponse) {
        Member member = Member.builder()
                .kakaoId(userInfoResponse.id())
                .email(userInfoResponse.getEmail())
                .nickname(userInfoResponse.getNickname())
                .profileImageUrl(userInfoResponse.getProfileImageUrl())
                .role(Role.USER)
                .build();

        return memberRepository.save(member);
    }

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