//여기서 감사 활성화하기~

package org.sopt.adapter.out.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(modifyOnCreation = false)
public class JpaAuditingConfig {
}
