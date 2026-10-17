package org.sopt.application.port.out;

import org.sopt.domain.Post;

public interface LoadPostPort {
    Post loadPost(Long postId);
}
