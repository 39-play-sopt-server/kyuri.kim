package org.sopt.adapter.in.controller;

import java.time.Instant;

import org.sopt.adapter.in.dto.request.LoginRequest;
import org.sopt.adapter.in.dto.request.SignUpRequest;
import org.sopt.adapter.in.dto.response.MemberResponse;
import org.sopt.adapter.in.dto.response.TokenResponse;
import org.sopt.application.service.AuthService;
import org.sopt.application.service.TokenService;
import org.sopt.common.response.ApiResponse;
import org.sopt.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final boolean secure;
    private final String sameSite;

    public AuthController(
            AuthService authService,
            @Value("${app.cookie.secure}") boolean secure,
            @Value("${app.cookie.same-site}") String sameSite
    ) {
        this.authService = authService;
        this.secure = secure;
        this.sameSite = sameSite;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(@RequestBody SignUpRequest request) {
        Member member = authService.signUp(request.name(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), MemberResponse.from(member)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        return tokenResponse(authService.login(request.email(), request.password()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        return tokenResponse(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie().toString())
                .body(ApiResponse.success(null));
    }

    private ResponseEntity<ApiResponse<TokenResponse>> tokenResponse(TokenService.IssuedTokenPair pair) {
        long accessExpiresIn = pair.accessExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();
        long refreshMaxAge = pair.refreshExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pair.accessToken())
                .header(HttpHeaders.SET_COOKIE, refreshCookie(pair.refreshToken(), refreshMaxAge).toString())
                .body(ApiResponse.success(new TokenResponse("Bearer", accessExpiresIn)));
    }

    private ResponseCookie refreshCookie(String value, long seconds) {
        return ResponseCookie.from("refresh_token", value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/api/v1/auth")
                .maxAge(seconds)
                .build();
    }

    private ResponseCookie expiredCookie() {
        return refreshCookie("", 0);
    }
}