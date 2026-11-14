package org.sopt.application.service;

import org.sopt.domain.Member;

public class JwtService {
    String createAccessToken(Member member);
    String createRefreshToken(Member member);
    boolean isTokenExpired(String token);
    String getCategory(String token);
}
