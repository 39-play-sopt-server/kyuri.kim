package org.sopt.application.service;

import org.sopt.application.port.out.MemberRepository;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.MemberErrorCode;
import org.sopt.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberRepository members;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokens;
    private final JwtDecoder jwtDecoder;

    public AuthService(MemberRepository members,
                       PasswordEncoder passwordEncoder,
                       TokenService tokens,
                       JwtDecoder jwtDecoder) {
        this.members = members;
        this.passwordEncoder = passwordEncoder;
        this.tokens = tokens;
        this.jwtDecoder = jwtDecoder;
    }

    @Transactional
    public Member signUp(String name, String email, String password) {
        String normalized = normalizeEmail(email);

        if (members.findByEmail(normalized).isPresent()) {
            throw new BusinessException(MemberErrorCode.DUPLICATE_EMAIL);
        }
        if (password == null || password.length() < 8) {
            throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        }

        return members.save(
                Member.register(name, normalized, passwordEncoder.encode(password))
        );
    }

    @Transactional(readOnly = true)
    public TokenService.IssuedTokenPair login(String email, String password) {
        Member member = members.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new BusinessException(MemberErrorCode.INVALID_CREDENTIALS));

        if (member.isDeleted()
                || member.getPasswordHash() == null
                || !passwordEncoder.matches(password, member.getPasswordHash())) {
            throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        }

        return tokens.issue(member);
    }

    @Transactional(readOnly = true)
    public TokenService.IssuedTokenPair socialLogin(String kakaoId, String nickname) {
        Member member = members.findByKakaoId(kakaoId)
                .orElseGet(() -> members.save(
                        Member.social(
                                nickname == null || nickname.isBlank() ? "카카오 사용자" : nickname,
                                kakaoId
                        )
                ));

        if (member.isDeleted()) {
            throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        }

        return tokens.issue(member);
    }

    @Transactional(readOnly = true)
    public TokenService.IssuedTokenPair refresh(String rawToken) {
        Jwt jwt = decodeRefresh(rawToken);

        Member member = members.findById(Long.valueOf(jwt.getSubject()))
                .orElseThrow(() -> new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN));

        if (member.isDeleted()) {
            throw new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN);
        }

        return tokens.issue(member);
    }

    // private 메서드에는 AOP 프록시가 적용 안 됨
    private Jwt decodeRefresh(String raw) {
        try {
            Jwt jwt = jwtDecoder.decode(raw);
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

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        }
        return email.trim().toLowerCase();
    }
}