package org.sopt.application.port.out;

import org.sopt.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member save(Member member);
    Optional<Member> findById(Long id);

    Optional<Member>findByEmail(String email);
    boolean existsByEmail(String email);
}