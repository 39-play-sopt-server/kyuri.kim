package org.sopt.domain;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;

import java.util.Arrays;

public enum Category {
    FREE("자유게시판"),
    INFO("정보게시판"),
    CAREER("취업 게시판");

    private final String display;

    Category(String display){
        this.display = display;
    }

    public String getDisplay(){return display;}

    public static Category fromString(String input){
        if(input == null || input.isBlank()){
            throw new BusinessException(PostErrorCode.EMPTY_POST_CATEGORY);
        }
        String normalized = input.trim().toUpperCase();
        return Arrays.stream(Category.values())
                .filter(c -> c.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new BusinessException(PostErrorCode.INVALID_CATEGORY));
    }
}