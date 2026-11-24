package org.sopt.adapter.in.controller;

import org.sopt.common.response.ApiResponse;
import org.springframework.security.oauth2.jwt.Jwt;

public interface MemberApi {
    ApiResponse<Void> withdrawMember(Jwt jwt);
}