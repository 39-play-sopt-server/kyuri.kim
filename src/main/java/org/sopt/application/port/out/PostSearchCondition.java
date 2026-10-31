package org.sopt.application.port.out;

import org.sopt.domain.Category;

public record PostSearchCondition(
        Long memberId,
        Category category,
        String title
) {
}