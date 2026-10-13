package org.sopt.application.service;

import org.sopt.application.port.in.IncreasePostViewCountUseCase;
import org.sopt.application.port.out.LoadPostPort;
import org.sopt.application.port.out.SavePostPort;
import org.sopt.domain.Post;
import org.springframework.stereotype.Service;

@Service

public class PostViewCountService implements IncreasePostViewCountUseCase {
    private final LoadPostPort loadPostPort;
    private final SavePostPort savePostPort;

    public PostViewCountService(LoadPostPort loadPostPort, SavePostPort savePostPort) {
        this.loadPostPort = loadPostPort;
        this.savePostPort = savePostPort;
    }

    @Override
    public void increaseViewCount(Long postId) {
        // 1. 게시글 조회
        Post post = loadPostPort.loadPost(postId);

        // 2. 도메인 메서드를 통해 조회수 증가
        post.increaseViewCount();

        // 3. 변경된 상태 저장
        savePostPort.savePost(post);
    }
}