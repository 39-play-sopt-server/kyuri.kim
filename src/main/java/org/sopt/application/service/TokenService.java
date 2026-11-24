package org.sopt.application.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.MemberErrorCode;
import org.sopt.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration accessTtl;
    private final Duration refreshTtl;
    public TokenService(JwtEncoder encoder, JwtDecoder decoder,
                        @Value("${app.jwt.access-token-ttl}") Duration accessTtl, @Value("${app.jwt.refresh-token-ttl}") Duration refreshTtl) {
        this.encoder=encoder;
        this.decoder = decoder;
        this.accessTtl=accessTtl; this.refreshTtl=refreshTtl;
    }
    public IssuedTokenPair issue(Member member) {
        Instant now=Instant.now();
        String access = encode(member, "access", now.plus(accessTtl));
        String refresh = encode(member, "refresh", now.plus(refreshTtl));
        return new IssuedTokenPair(access, refresh, now.plus(accessTtl), now.plus(refreshTtl));
    }
    private String encode(Member m, String use, Instant expiry) {
        JwtClaimsSet.Builder b=JwtClaimsSet.builder().subject(m.getId().toString()).expiresAt(expiry).claim("token_use",use);
        if ("access".equals(use)) b.claim("roles", List.of(m.getRole().name()));
        return encoder.encode(JwtEncoderParameters.from(b.build())).getTokenValue();
    }

    public Jwt decodeRefreshToken(String rawToken) {
        try {
            Jwt jwt = decoder.decode(rawToken);
            if (!"refresh".equals(jwt.getClaimAsString("token_use"))) {
                throw new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN);
            }
            return jwt;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public long accessExpiresInSeconds() { return accessTtl.toSeconds(); }
    public Duration refreshTtl() { return refreshTtl; }
    public record IssuedTokenPair(String accessToken, String refreshToken, Instant accessExpiresAt, Instant refreshExpiresAt) {}
}
