package org.sopt.application.port.out;

import org.sopt.domain.Member;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByKakaoId(String kakaoId);
    Page<Member> findAll(Pageable pageable);
}