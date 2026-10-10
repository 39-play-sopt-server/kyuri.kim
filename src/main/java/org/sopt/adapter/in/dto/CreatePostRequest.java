package org.sopt.adapter.in.dto;

public record CreatePostRequest (
    String title,
    String content,
    String writer,
    String category
){}