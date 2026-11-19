package org.sopt.adapter.in.controller;

import org.sopt.adapter.in.dto.request.CreateMemberRequest;
import org.sopt.adapter.in.dto.response.MemberResponse;
import org.sopt.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

public interface MemberApi {
    ResponseEntity<ApiResponse<MemberResponse>> createMember(
            CreateMemberRequest request
    );

    @GetMapping("/me")
    ResponseEntity<Long> me(@AuthenticationPrincipal Long memberId);

    ApiResponse<Void> withdrawMember(Long memberId);
}