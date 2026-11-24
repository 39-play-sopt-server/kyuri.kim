package org.sopt.adapter.in.controller;

import org.sopt.adapter.in.dto.request.CreateMemberRequest;
import org.sopt.adapter.in.dto.response.MemberResponse;
import org.sopt.application.port.in.MemberUseCase;
import org.sopt.application.service.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.sopt.common.response.ApiResponse;
import org.sopt.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController implements MemberApi {

    private final MemberUseCase memberUseCase;
    private final AuthService authService;

    public MemberController(MemberUseCase memberUseCase, AuthService authService) {
        this.memberUseCase = memberUseCase;
        this.authService = authService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @RequestBody CreateMemberRequest request
    ) {
        Member member = memberUseCase.createMember(request.toCommand());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        MemberResponse.from(member)
                ));
    }

    @Override
    @DeleteMapping("/me")
    public ApiResponse<Void> withdrawMember(@AuthenticationPrincipal Jwt jwt
    ) {
        Long memberId=Long.valueOf(jwt.getSubject());
        memberUseCase.withdrawMember(memberId);
        authService.revokeAll(memberId);
        return ApiResponse.success(null);
    }
}
