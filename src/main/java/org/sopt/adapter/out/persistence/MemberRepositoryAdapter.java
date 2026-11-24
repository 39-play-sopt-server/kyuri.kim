package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.MemberRepository;
import org.sopt.domain.Member;
import org.springframework.stereotype.Component;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Component
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id);
    }
    @Override public Optional<Member> findByEmail(String email) { return memberJpaRepository.findByEmail(email); }
    @Override public Page<Member> findAll(Pageable pageable) { return memberJpaRepository.findAll(pageable); }
}
