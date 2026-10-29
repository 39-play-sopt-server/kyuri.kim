package org.sopt.application.service;

import org.sopt.application.port.in.CreateMemberCommand;
import org.sopt.application.port.in.MemberUseCase;
import org.sopt.application.port.out.MemberRepository;
import org.sopt.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements MemberUseCase {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public Member createMember(CreateMemberCommand command) {
        Member member = Member.create(command.name());
        return memberRepository.save(member);
    }
}