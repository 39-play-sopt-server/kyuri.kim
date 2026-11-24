package org.sopt.application.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import org.sopt.adapter.out.persistence.AuthSessionJpaRepository;
import org.sopt.adapter.out.persistence.RefreshTokenJpaRepository;
import org.sopt.adapter.out.persistence.OAuthIdentityJpaRepository;
import org.sopt.application.port.out.MemberRepository;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.MemberErrorCode;
import org.sopt.domain.AuthSession;
import org.sopt.domain.Member;
import org.sopt.domain.RefreshTokenRecord;
import org.sopt.domain.OAuthIdentity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final MemberRepository members; private final AuthSessionJpaRepository sessions; private final RefreshTokenJpaRepository refreshRecords;
    private final PasswordEncoder passwordEncoder; private final TokenService tokens; private final JwtDecoder jwtDecoder;
    private final OAuthIdentityJpaRepository identities;
    public AuthService(MemberRepository members, AuthSessionJpaRepository sessions, RefreshTokenJpaRepository refreshRecords, OAuthIdentityJpaRepository identities, PasswordEncoder passwordEncoder, TokenService tokens, @Qualifier("jwtDecoder") JwtDecoder jwtDecoder) {
        this.members=members; this.sessions=sessions; this.refreshRecords=refreshRecords; this.identities=identities; this.passwordEncoder=passwordEncoder; this.tokens=tokens; this.jwtDecoder=jwtDecoder;
    }
    @Transactional
    public Member signUp(String name, String email, String password) {
        String normalized=normalizeEmail(email); if (members.findByEmail(normalized).isPresent()) throw new BusinessException(MemberErrorCode.DUPLICATE_EMAIL);
        if (password == null || password.length() < 8) throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        return members.save(Member.register(name, normalized, passwordEncoder.encode(password)));
    }
    @Transactional
    public TokenService.IssuedTokenPair login(String email, String password) {
        Member member=members.findByEmail(normalizeEmail(email)).orElseThrow(() -> new BusinessException(MemberErrorCode.INVALID_CREDENTIALS));
        if (member.isDeleted() || member.getPasswordHash() == null || !passwordEncoder.matches(password, member.getPasswordHash())) throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        return createSessionTokens(member);
    }
    @Transactional
    public TokenService.IssuedTokenPair socialLogin(String provider, String subject, String nickname) {
        Member member=identities.findByProviderAndProviderSubject(provider, subject).map(OAuthIdentity::getMember).orElseGet(() -> {
            Member created=members.save(Member.social(nickname == null || nickname.isBlank() ? provider + " 사용자" : nickname));
            identities.save(new OAuthIdentity(provider, subject, created));
            return created;
        });
        if(member.isDeleted()) throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS);
        return createSessionTokens(member);
    }
    @Transactional
    public TokenService.IssuedTokenPair refresh(String rawToken) {
        Jwt jwt=decodeRefresh(rawToken);
        RefreshTokenRecord record=refreshRecords.findByJtiForUpdate(jwt.getId()).orElseThrow(() -> new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN));
        AuthSession session=record.getSession();
        if (!record.isUsable(hash(rawToken)) || !session.isActive()) { session.revoke(); throw new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN); }
        record.consume();
        return createTokensForExistingSession(session);
    }
    @Transactional
    public void logout(String rawToken) { AuthSession s=sessionFromRefresh(rawToken); s.revoke(); }
    @Transactional
    public void revokeAll(Long memberId) { sessions.findAll().stream().filter(s -> s.getMember().getId().equals(memberId)).forEach(AuthSession::revoke); }
    private TokenService.IssuedTokenPair createSessionTokens(Member member) { AuthSession s=sessions.save(AuthSession.create(member, Instant.now().plus(tokens.refreshTtl()))); return createTokensForExistingSession(s); }
    private TokenService.IssuedTokenPair createTokensForExistingSession(AuthSession s) { TokenService.IssuedTokenPair pair=tokens.issue(s.getMember(),s); refreshRecords.save(RefreshTokenRecord.create(s,pair.refreshJti(),hash(pair.refreshToken()),pair.refreshExpiresAt())); return pair; }
    private AuthSession sessionFromRefresh(String raw) { Jwt jwt=decodeRefresh(raw); return sessions.findByIdForUpdate(java.util.UUID.fromString(jwt.getClaimAsString("sid"))).orElseThrow(() -> new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN)); }
    private Jwt decodeRefresh(String raw) { try { Jwt jwt=jwtDecoder.decode(raw); if (!"refresh".equals(jwt.getClaimAsString("token_use"))) throw new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN); return jwt; } catch (BusinessException e) { throw e; } catch (Exception e) { throw new BusinessException(MemberErrorCode.INVALID_REFRESH_TOKEN); } }
    private String normalizeEmail(String email) { if(email==null || email.isBlank() || !email.contains("@")) throw new BusinessException(MemberErrorCode.INVALID_CREDENTIALS); return email.trim().toLowerCase(); }
    private String hash(String v) { try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(v.getBytes(StandardCharsets.UTF_8))); } catch(Exception e){ throw new IllegalStateException(e); } }
}
