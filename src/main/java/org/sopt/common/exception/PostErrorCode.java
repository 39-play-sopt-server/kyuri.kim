package org.sopt.common.exception;

import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode{
    //400 - BAD_REQUEST - 잘못 요청
    EMPTY_POST_TITLE(HttpStatus.BAD_REQUEST, "POST-001", "게시글 제목은 비워둘 수 없습니다"),
    EMPTY_POST_CONTENT(HttpStatus.BAD_REQUEST, "POST-002", "게시글 내용은 비워둘 수 없습니다"),
    EMPTY_POST_WRITER(HttpStatus.BAD_REQUEST, "POST-003", "게시글 작성자는 비워둘 수 없습니다"),
    EMPTY_POST_CATEGORY(HttpStatus.BAD_REQUEST, "POST-004", "게시글 카테고리는 비워둘 수 없습니다"),
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "POST-005", "제목은 최대 50자까지 입력 가능합니다"),
    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "POST-006", "내용은 최대 500자까지 입력 가능합니다"),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "POST-007", "존재하지 않는 게시글 카테고리입니다"),
    INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, "POST-010", "page는 1이상, size는 1이상 100 이하여야 합니다"),

    INVALID_CREATED_AT(HttpStatus.INTERNAL_SERVER_ERROR, "POST-008", "작성일이 유효하지 않습니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-009", "존재하지 않는 게시글입니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    PostErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
    @Override
    public HttpStatus getStatus(){return httpStatus;}

    @Override
    public String getCode(){
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }
}

