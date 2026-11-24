package org.sopt.common.exception;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-001", "존재하지 않는 회원입니다"),
    MEMBER_WITHDRAWN(HttpStatus.BAD_REQUEST,"MEMBER-002", "탈퇴한 회원은 게시글을 작성할 수 없습니다"),
    EMPTY_MEMBER_NAME(HttpStatus.BAD_REQUEST, "MEMBER-003", "회원 이름은 비워둘 수 없습니다"),
    MEMBER_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "MEMBER-004", "회원 이름은 최대 50자까지 입력 가능합니다"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER-005", "이미 사용 중인 이메일입니다"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "MEMBER-006", "이메일 또는 비밀번호가 올바르지 않습니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "MEMBER-007", "유효하지 않은 Refresh Token입니다"),
    FORBIDDEN_POST_ACCESS(HttpStatus.FORBIDDEN, "MEMBER-008", "게시글을 수정하거나 삭제할 권한이 없습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    MemberErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
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
