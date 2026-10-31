package org.sopt.application.port.out;

import org.sopt.domain.Post;

import java.util.List;

// 내용 + 전체 개수를 한 번에

public record PostSearchResult(
        List<Post> posts,
        long totalElements
) {
}