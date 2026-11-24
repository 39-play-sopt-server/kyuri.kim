package org.sopt.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.sopt.domain.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;

public interface AuthSessionJpaRepository extends JpaRepository<AuthSession, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from AuthSession s join fetch s.member where s.id = :id")
    Optional<AuthSession> findByIdForUpdate(@Param("id") UUID id);
}
