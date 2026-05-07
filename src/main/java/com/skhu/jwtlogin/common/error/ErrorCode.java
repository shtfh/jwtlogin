package com.skhu.jwtlogin.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사에 실패하였습니다.", "BAD_REQUEST_400"),

    INVALID_JWT(HttpStatus.UNAUTHORIZED, "JWT가 비어있거나 잘못된 값입니다.", "UNAUTHORIZED_401"),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 없습니다.", "NOT_FOUND_404"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생햇습니다.", "INTERNAL_SERVER_ERROR_500");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}