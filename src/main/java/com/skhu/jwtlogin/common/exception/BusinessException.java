package com.skhu.jwtlogin.common.exception;

import com.skhu.jwtlogin.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String customMessage;

    public BusinessException(
            ErrorCode errorCode,
            String customMessage
    ) {
        super(customMessage);

        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }
}