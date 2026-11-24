package org.sopt.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_token_records", indexes = @Index(name = "idx_refresh_jti", columnList = "jti", unique = true))
public class RefreshTokenRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "session_id", nullable = false) private AuthSession session;
    @Column(nullable = false, unique = true, length = 64) private String jti;
    @Column(nullable = false, length = 64) private String tokenHash;
    @Column(nullable = false) private Instant expiresAt;
    private Instant usedAt;
    protected RefreshTokenRecord() {}
    private RefreshTokenRecord(AuthSession session, String jti, String tokenHash, Instant expiresAt) { this.session=session; this.jti=jti; this.tokenHash=tokenHash; this.expiresAt=expiresAt; }
    public static RefreshTokenRecord create(AuthSession s,String j,String h,Instant e){ return new RefreshTokenRecord(s,j,h,e); }
    public boolean isUsable(String hash) { return usedAt == null && expiresAt.isAfter(Instant.now()) && tokenHash.equals(hash); }
    public void consume() { usedAt = Instant.now(); }
    public AuthSession getSession(){return session;}
}
