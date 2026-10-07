package org.sopt.domain;

import java.util.Arrays;

public enum Category {
    FREE("FREE", "자유게시판"),
    INFORMATION("IMFO", "정보게시판"),
    CAREER("CAREER", "취업 게시판");

    private final String code;
    private final String title;

    Category(String code, String title){
        this.code = code;
        this.title = title;
    }

    public String getCode(){return code;}
    public String getTitle(){return title;}

    public static Category fromCode(String code){
        return Arrays.stream(Category.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + code));
    }
}