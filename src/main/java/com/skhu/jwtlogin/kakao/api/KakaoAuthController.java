package com.skhu.jwtlogin.kakao.api;

import com.skhu.jwtlogin.common.error.SuccessCode;
import com.skhu.jwtlogin.common.template.ApiResTemplate;
import com.skhu.jwtlogin.kakao.api.dto.KakaoTokenResponse;
import com.skhu.jwtlogin.kakao.api.dto.KakaoUserInfoResponse;
import com.skhu.jwtlogin.kakao.application.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/callback")
    public ApiResTemplate<?> kakaoCallback(
            @RequestParam("code") String code
    ) {
        KakaoTokenResponse tokenResponse = kakaoAuthService.requestToken(code);

        KakaoUserInfoResponse userInfoResponse =
                kakaoAuthService.requestUserInfo(tokenResponse.accessToken());

        return ApiResTemplate.successResponse(
                SuccessCode.LOGIN_SUCCESS,
                userInfoResponse
        );
    }
}