package org.sopt.adapter.in.dto.request;

import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.domain.Category;

public record CreatePostRequest (
    String title,
    String content,
    Long memberId,
    String category
){
    public CreatePostCommand toCommand() {
        Category domainCategory = Category.fromString(this.category);
        return new CreatePostCommand(this.title, this.content, this.memberId, domainCategory);
    }
}
