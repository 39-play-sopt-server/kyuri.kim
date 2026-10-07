package org.sopt.application.port.out;

//아웃바운드 포트 (repository 인터페이스) | "게시글을 저장한다"는 인터페이스 정의

import org.sopt.domain.Post;

public interface PostRepository {
    Post save(Post post);
}
