package org.sopt.adapter.in.dto;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

//응답 DTO
public record PostResponse(Long id, String title, String content, String writer, String category, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter(),
                post.getCategory().name(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}