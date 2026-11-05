package org.sopt.application.port.out;

import org.sopt.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
}