package org.sopt.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하여야 합니다.")
        String password
) {}
