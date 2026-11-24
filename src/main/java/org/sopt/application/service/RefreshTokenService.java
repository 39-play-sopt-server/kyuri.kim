package org.sopt.application.service;

import org.sopt.domain.Member;

public abstract class RefreshTokenService {
    void addRefreshTokenEntity(Member member, String refreshToken) {

    }

    void removeRefreshToken(String refreshToken) {

    }

    abstract String reissueAccessToken(String refreshToken);
}
