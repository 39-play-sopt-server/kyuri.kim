package org.sopt.adapter.out.persistence;

import java.util.Optional;
import org.sopt.domain.OAuthIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthIdentityJpaRepository extends JpaRepository<OAuthIdentity, Long> {
    Optional<OAuthIdentity> findByProviderAndProviderSubject(String provider, String providerSubject);
}
