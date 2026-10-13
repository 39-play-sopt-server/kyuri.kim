package org.sopt.common.exception;

import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 400, "COMMON_001", "잘못된 입력값입니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, 400, "COMMON_002", "요청 본문 JSON 형식이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "COMMON_003", "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "COMMON_500", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final int httpStatus;
    private final String code;
    private final String message;

    GlobalErrorCode(HttpStatus status, int httpStatus, String code, String message) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}