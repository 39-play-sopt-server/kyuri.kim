package org.sopt.adapter.in.dto.request;

import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.domain.Category;

public record CreatePostRequest(
        String title,
        String content,
        String category
) {
    public CreatePostCommand toCommand(Long memberId) {
        return new CreatePostCommand(
                this.title,
                this.content,
                memberId,
                Category.fromString(this.category)
        );
    }
}