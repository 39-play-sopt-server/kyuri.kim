package org.sopt.common.exception;


public enum PostErrorCode {
    //400 - BAD_REQUEST - 잘못 요청
    EMPTY_POST_TITLE(400, "POST-001", "게시글 제목은 비워둘 수 없습니다"),
    EMPTY_POST_CONTENT(400, "POST-002", "게시글 내용은 비워둘 수 없습니다"),
    EMPTY_POST_WRITER(400, "POST-003", "게시글 작성자는 비워둘 수 없습니다"),
    EMPTY_POST_CATEGORY(400, "POST-004", "게시글 카테고리는 비워둘 수 없습니다"),
    TITLE_TOO_LONG(400, "POST-005", "제목은 최대 50자까지 입력 가능합니다"),
    CONTENT_TOO_LONG(400, "POST-006", "내용은 최대 500자까지 입력 가능합니다"),
    INVALID_CATEGORY(400, "POST-007", "존재하지 않는 게시글 카테고리입니다"),

    INVALID_CREATED_AT(500, "POST-008", "작성일이 유효하지 않습니다"),
    POST_NOT_FOUND(404, "POST-009", "존재하지 않는 게시글입니다");

    private final int httpStatus;
    private final String code;
    private final String message;

    PostErrorCode(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus(){
        return httpStatus;
    }
    public String getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
}

