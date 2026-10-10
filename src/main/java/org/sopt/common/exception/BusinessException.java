package org.sopt.common.exception;

//공통 예외 묶기
/*RuntimeException이랑 Exception 상속이 무엇이 다른가에 대한 공부..*/

public class BusinessException extends RuntimeException{

    private final PostErrorCode errorCode;

    public BusinessException(PostErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public PostErrorCode getErrorCode(){
        return errorCode;
    }
}
