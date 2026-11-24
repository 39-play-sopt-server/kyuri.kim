package org.sopt.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auth_sessions", indexes = @Index(name = "idx_auth_session_member", columnList = "member_id"))
public class AuthSession {
    @Id private UUID id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false) private Member member;
    @Column(nullable = false) private Instant expiresAt;
    private Instant revokedAt;
    protected AuthSession() {}
    private AuthSession(Member member, Instant expiresAt) { this.id = UUID.randomUUID(); this.member = member; this.expiresAt = expiresAt; }
    public static AuthSession create(Member member, Instant expiresAt) { return new AuthSession(member, expiresAt); }
    public void revoke() { revokedAt = Instant.now(); }
    public boolean isActive() { return revokedAt == null && expiresAt.isAfter(Instant.now()); }
    public UUID getId() { return id; }
    public Member getMember() { return member; }
    public Instant getExpiresAt() { return expiresAt; }
}
