package org.sopt.application.port.in;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Category;

public record GetPostsCommand(
        int page,
        int size,
        Category category,
        String title
) {
    private static final int MAX_PAGE_SIZE = 100;

    public GetPostsCommand(int page, int size, Category category, String title) {
        if (page < 1 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new BusinessException(PostErrorCode.INVALID_PAGE_REQUEST);
        }
        this.page = page;
        this.size = size;
        this.category = category;
        this.title= title;
    }
}