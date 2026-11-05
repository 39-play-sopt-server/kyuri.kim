package org.sopt.application.service;

import org.sopt.application.port.in.IncreasePostViewCountUseCase;
import org.sopt.application.port.out.IncreaseViewCountPort;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class PostViewCountService implements IncreasePostViewCountUseCase {
    //JPA 방식으로 바꾸기
    private final IncreaseViewCountPort increaseViewCountPort;

    public PostViewCountService(IncreaseViewCountPort increaseViewCountPort) {
        this.increaseViewCountPort = increaseViewCountPort;
    }

    @Override
    @Transactional
    public void increaseViewCount(Long postId) {
        int updated = increaseViewCountPort.increaseViewCount(postId);
        if (updated == 0) {
            throw new BusinessException(PostErrorCode.POST_NOT_FOUND);
        }
    }
}