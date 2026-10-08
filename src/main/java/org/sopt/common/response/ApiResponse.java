package org.sopt.common.response;

//공통 응답 객체 (제너릭)

import org.sopt.common.exception.PostErrorCode;

public class ApiResponse<T> {
    private final boolean success;
    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, int status, String message, T data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    //성공 & 데이터 O
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, 200, "성공", data);
    }

    //실패
    public static <T> ApiResponse<T> fail(PostErrorCode errorCode){
        return new ApiResponse<>(false, errorCode.getHttpStatus(), errorCode.getMessage(), null);
    }

    public boolean isSuccess(){return success;}
    public int getStatus(){return status;}
    public String getMessage(){return message;}
    public T getData(){return data;}

}
