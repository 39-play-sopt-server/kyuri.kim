package org.sopt.adapter.in.security;

import java.util.UUID;
import org.sopt.adapter.out.persistence.AuthSessionJpaRepository;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;

public class SessionStatusJwtValidator implements OAuth2TokenValidator<Jwt> {
    private final AuthSessionJpaRepository sessions;
    public SessionStatusJwtValidator(AuthSessionJpaRepository sessions) { this.sessions=sessions; }
    @Override public OAuth2TokenValidatorResult validate(Jwt token) {
        try {
            if (!"access".equals(token.getClaimAsString("token_use"))) return failure();
            UUID sid=UUID.fromString(token.getClaimAsString("sid"));
            return sessions.findById(sid).filter(s -> s.isActive() && s.getMember().getId().toString().equals(token.getSubject())).isPresent() ? OAuth2TokenValidatorResult.success() : failure();
        } catch (Exception e) { return failure(); }
    }
    private OAuth2TokenValidatorResult failure() { return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "The session is not active", null)); }
}
