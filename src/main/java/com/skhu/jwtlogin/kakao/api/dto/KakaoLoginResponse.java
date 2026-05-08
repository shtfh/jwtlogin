package com.skhu.jwtlogin.kakao.api.dto;

public record KakaoLoginResponse(
        Long memberId,
        String accessToken,
        String nickname
) {
}