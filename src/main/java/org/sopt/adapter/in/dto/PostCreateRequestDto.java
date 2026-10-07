package org.sopt.adapter.in.dto;

public record PostCreateRequestDto(
        String title,
        String content,
        Long authorId
) {
}