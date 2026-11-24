package org.sopt.adapter.out.persistence;

import java.util.Optional;
import org.sopt.domain.RefreshTokenRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenRecord, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RefreshTokenRecord r join fetch r.session s join fetch s.member where r.jti = :jti")
    Optional<RefreshTokenRecord> findByJtiForUpdate(@Param("jti") String jti);
}
