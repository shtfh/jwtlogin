package com.skhu.jwtlogin.common.template;

import com.skhu.jwtlogin.common.error.ErrorCode;
import com.skhu.jwtlogin.common.error.SuccessCode;
import lombok.*;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ApiResTemplate<T> {

    private final int code;
    private final String message;
    private T data;

    public static ApiResTemplate successWithNoContent(SuccessCode successCode) {
        return new ApiResTemplate(
                successCode.getHttpStatusCode(),
                successCode.getMessage()
        );
    }

    public static <T> ApiResTemplate<T> successResponse(
            SuccessCode successCode,
            T data
    ) {
        return new ApiResTemplate<>(
                successCode.getHttpStatusCode(),
                successCode.getMessage(),
                data
        );
    }

    public static ApiResTemplate errorResponse(
            ErrorCode errorCode,
            String customMessage
    ) {
        return new ApiResTemplate(
                errorCode.getHttpStatusCode(),
                customMessage
        );
    }
}