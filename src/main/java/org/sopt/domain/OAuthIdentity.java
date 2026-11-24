package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "oauth_identities", uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_subject"}))
public class OAuthIdentity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String provider;
    @Column(name = "provider_subject", nullable = false) private String providerSubject;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false) private Member member;
    protected OAuthIdentity() {}
    public OAuthIdentity(String provider, String providerSubject, Member member) { this.provider=provider; this.providerSubject=providerSubject; this.member=member; }
    public Member getMember() { return member; }
    public String getProvider() { return provider; }
    public String getProviderSubject() { return providerSubject; }
}
