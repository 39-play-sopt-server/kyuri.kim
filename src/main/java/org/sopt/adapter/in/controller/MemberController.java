package org.sopt.adapter.in.controller;

import org.sopt.adapter.in.dto.request.CreateMemberRequest;
import org.sopt.adapter.in.dto.response.MemberResponse;
import org.sopt.application.port.in.MemberUseCase;
import org.sopt.common.response.ApiResponse;
import org.sopt.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController implements MemberApi {

    private final MemberUseCase memberUseCase;

    public MemberController(MemberUseCase memberUseCase) {
        this.memberUseCase = memberUseCase;
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
    @DeleteMapping("/{memberId}")
    public ApiResponse<Void> withdrawMember(
            @PathVariable Long memberId
    ) {
        memberUseCase.withdrawMember(memberId);
        return ApiResponse.success(null);
    }
}