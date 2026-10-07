package org.sopt.domain;

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
            throw new IllegalArgumentException("카테고리는 비워둘 수 없습니다");
        }
        String normalized = input.trim().toUpperCase();
        return Arrays.stream(Category.values())
                .filter(c -> c.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + input));
    }
}