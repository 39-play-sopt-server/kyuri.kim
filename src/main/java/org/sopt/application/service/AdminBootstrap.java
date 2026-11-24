package org.sopt.application.service;

import org.sopt.application.port.out.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AdminBootstrap {
    @Bean
    ApplicationRunner grantConfiguredAdmin(MemberRepository members, @Value("${app.admin-email:}") String adminEmail) {
        return args -> grant(members, adminEmail);
    }
    @Transactional
    void grant(MemberRepository members, String adminEmail) {
        if (adminEmail == null || adminEmail.isBlank()) return;
        members.findByEmail(adminEmail.trim().toLowerCase()).ifPresent(member -> { member.grantAdmin(); members.save(member); });
    }
}
