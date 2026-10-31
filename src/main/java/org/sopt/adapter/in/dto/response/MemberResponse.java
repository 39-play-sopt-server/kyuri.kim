package org.sopt.adapter.in.dto.response;

import org.sopt.domain.Member;

public record MemberResponse(Long id, String name) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName()
        );
    }
}