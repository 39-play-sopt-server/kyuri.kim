package org.sopt.adapter.in.config;

import org.sopt.adapter.in.security.RoleJwtAuthenticationConverter;
import org.sopt.adapter.in.security.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration @EnableWebSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean CookieCsrfTokenRepository csrfTokenRepository() { return CookieCsrfTokenRepository.withHttpOnlyFalse(); }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder decoder, OAuth2LoginSuccessHandler successHandler, CookieCsrfTokenRepository csrfTokenRepository) throws Exception {
        http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository)
                .ignoringRequestMatchers("/api/v1/auth/signup", "/api/v1/auth/login", "/oauth2/**", "/login/**"))
            .cors(Customizer.withDefaults()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(a -> a.requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/**", "/oauth2/**", "/login/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/posts", "/api/v1/posts/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/members").hasRole("ADMIN").anyRequest().authenticated())
            .oauth2Login(o -> o.successHandler(successHandler))
            .oauth2ResourceServer(o -> o.jwt(j -> j.decoder(decoder).jwtAuthenticationConverter(new RoleJwtAuthenticationConverter())));
        return http.build();
    }
}
