package org.sopt.adapter.in.dto.response;

import org.sopt.domain.Post;
import java.time.LocalDateTime;

//응답 DTO
public record PostResponse(Long id, String title, String content, String writer, String category, LocalDateTime createdAt, LocalDateTime updatedAt, long viewCount) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getDisplayName(),
                post.getCategory().name(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViewCount()
        );
    }
}