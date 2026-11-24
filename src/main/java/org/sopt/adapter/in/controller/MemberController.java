package org.sopt.adapter.in.controller;

import org.sopt.application.port.in.MemberUseCase;
import org.sopt.common.response.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberUseCase memberUseCase;

    public MemberController(MemberUseCase memberUseCase) {
        this.memberUseCase = memberUseCase;
    }

    @DeleteMapping("/me")
    public ApiResponse<Void> withdrawMember(@AuthenticationPrincipal Jwt jwt) {
        memberUseCase.withdrawMember(Long.valueOf(jwt.getSubject()));
        return ApiResponse.success(null);
    }
}
