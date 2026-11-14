package org.sopt.application;

import org.sopt.domain.RefreshToken;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationService implements LoginUseCase {

    public LoginResult login(LoginCommand command) {
        // 1. 사용자 인증
        Member member = authenticateUser(command.account(), command.password());

        // 2. 사용자 상태 검증 (잠김, 비활성 등)
        validateUserStatus(member);

        // 3. RefreshToken 생성 및 저장
        RefreshToken refreshToken = createAndSaveRefreshToken(member.getMemberId());

        // 4. AccessToken 생성
        String accessToken = jwtProvider.generateAccessToken(
                member.getMemberId(),
                refreshToken.getId()
        );

        return LoginResult.from(member, accessToken);
    }
}