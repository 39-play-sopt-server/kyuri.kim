package org.sopt.application.port.in;

import org.sopt.domain.Member;

public interface MemberUseCase {
    Member createMember(CreateMemberCommand command);
}
//이렇게 되면, 컨트롤러는 MemberService(구현 클래스) 직접 안 불러도 애 통해서 회원 생성을 요청할 수 있음.!