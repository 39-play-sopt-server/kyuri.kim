package org.sopt.application.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.sopt.domain.AuthSession;
import org.sopt.domain.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtEncoder encoder;
    private final String issuer;
    private final String audience;
    private final Duration accessTtl;
    private final Duration refreshTtl;
    public TokenService(JwtEncoder encoder, @Value("${app.jwt.issuer}") String issuer, @Value("${app.jwt.audience}") String audience,
                        @Value("${app.jwt.access-token-ttl}") Duration accessTtl, @Value("${app.jwt.refresh-token-ttl}") Duration refreshTtl) {
        this.encoder=encoder; this.issuer=issuer; this.audience=audience; this.accessTtl=accessTtl; this.refreshTtl=refreshTtl;
    }
    public IssuedTokenPair issue(Member member, AuthSession session) {
        Instant now=Instant.now(); String refreshJti=UUID.randomUUID().toString();
        String access = encode(member, session, "access", UUID.randomUUID().toString(), now, now.plus(accessTtl));
        String refresh = encode(member, session, "refresh", refreshJti, now, now.plus(refreshTtl));
        return new IssuedTokenPair(access, refresh, refreshJti, now.plus(accessTtl), now.plus(refreshTtl));
    }
    private String encode(Member m, AuthSession s, String use, String jti, Instant now, Instant expiry) {
        JwtClaimsSet.Builder b=JwtClaimsSet.builder().issuer(issuer).subject(m.getId().toString()).audience(List.of(audience))
                .issuedAt(now).expiresAt(expiry).id(jti).claim("token_use",use).claim("sid",s.getId().toString());
        if ("access".equals(use)) b.claim("roles", List.of(m.getRole().name()));
        return encoder.encode(JwtEncoderParameters.from(b.build())).getTokenValue();
    }
    public long accessExpiresInSeconds() { return accessTtl.toSeconds(); }
    public Duration refreshTtl() { return refreshTtl; }
    public String issuer() { return issuer; }
    public String audience() { return audience; }
    public record IssuedTokenPair(String accessToken, String refreshToken, String refreshJti, Instant accessExpiresAt, Instant refreshExpiresAt) {}
}
