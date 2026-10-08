package org.sopt.common.exception;

//BUsinessException 잡아서 ApiResponse.fail로 변환

import org.sopt.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void>handleBusinessException(BusinessException e){
        return ApiResponse.fail(e.getErrorCode());
    }
}
