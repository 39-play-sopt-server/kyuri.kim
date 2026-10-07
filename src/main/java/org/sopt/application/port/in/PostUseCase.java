package org.sopt.application.port.in;

//인바운드 포트 (유스케이스) | "게시글을 작성한다"

import org.sopt.adapter.in.dto.PostDetailResponseDto;

public interface PostUseCase {
    PostDetailResponseDto createPost(CreatePostCommand command);
}
