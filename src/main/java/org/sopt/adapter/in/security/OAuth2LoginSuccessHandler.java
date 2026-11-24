package org.sopt.adapter.in.security;

import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.Instant;
import org.sopt.application.service.AuthService;
import org.sopt.application.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService; private final boolean secure; private final String sameSite; private final CookieCsrfTokenRepository csrfTokens;
    public OAuth2LoginSuccessHandler(AuthService authService, @Value("${app.cookie.secure}") boolean secure, @Value("${app.cookie.same-site}") String sameSite, CookieCsrfTokenRepository csrfTokens) { this.authService=authService; this.secure=secure; this.sameSite=sameSite; this.csrfTokens=csrfTokens; }
    @Override public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response,Authentication authentication) throws IOException {
        OAuth2User user=(OAuth2User)authentication.getPrincipal(); String subject=String.valueOf(user.getAttribute("id"));
        String nickname=null; Object properties=user.getAttribute("properties"); if(properties instanceof java.util.Map<?,?> map && map.get("nickname") != null) nickname=map.get("nickname").toString();
        TokenService.IssuedTokenPair pair=authService.socialLogin(subject,nickname);
        response.addHeader(HttpHeaders.SET_COOKIE, ResponseCookie.from("refresh_token",pair.refreshToken()).httpOnly(true).secure(secure).sameSite(sameSite).path("/api/v1/auth").maxAge(pair.refreshExpiresAt().getEpochSecond()-Instant.now().getEpochSecond()).build().toString());
        CsrfToken csrfToken=csrfTokens.generateToken(request);
        csrfTokens.saveToken(csrfToken, request, response);
        getRedirectStrategy().sendRedirect(request,response,"/");
    }
}
