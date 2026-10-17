package org.sopt.adapter.in.dto;

import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.domain.Category;

public record CreatePostRequest (
    String title,
    String content,
    String writer,
    String category
){
    public CreatePostCommand toCommand() {
        Category domainCategory = Category.fromString(this.category);
        return new CreatePostCommand(this.title, this.content, this.writer, domainCategory);
    }
}
