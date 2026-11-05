package org.sopt.adapter.in.controller;

import org.sopt.adapter.in.dto.request.CreateMemberRequest;
import org.sopt.adapter.in.dto.response.MemberResponse;
import org.sopt.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface MemberApi {
    ResponseEntity<ApiResponse<MemberResponse>> createMember(
            CreateMemberRequest request
    );
    ApiResponse<Void> withdrawMember(Long memberId);
}