package org.sopt.adapter.in.support;

import org.sopt.common.response.ApiResponse;
import java.util.function.Supplier;
import org.sopt.common.exception.BusinessException;

//공통 래퍼 클래스 만들어서 컨트롤러의 try-catch 중복과 응답 감싸는 로직 추출하기
public final class ControllerSupport {

    private ControllerSupport() {
        // 인스턴스화 방지
    }

    public static <T> ApiResponse<T> handle(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            return ApiResponse.success(result);
        } catch (BusinessException e) {
            return ApiResponse.fail(e.getErrorCode());
        }
    }

    public static ApiResponse<Void> handleVoid(Runnable runnable) {
        return handle(() -> {
            runnable.run();
            return null;
        });
    }
}