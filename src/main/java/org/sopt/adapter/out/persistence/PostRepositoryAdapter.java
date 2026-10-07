package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.SaveMemberPort;

import java.lang.reflect.Member;

@Repository
@RequiredArgsConstructor
public class MemberPersistetenceAdapter implements SaveMemberPort {

    private final MemberMapper memberMapper;

    private final MemberSpringDataRepository repository;

    @Override
    public void saveMember(Member member) {
        repository.save(memberMapper.toEntity(member));
    }
}