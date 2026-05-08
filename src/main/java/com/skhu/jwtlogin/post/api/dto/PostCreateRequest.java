package com.skhu.jwtlogin.post.api.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequest(

        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
}