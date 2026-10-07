package org.sopt.adapter.in;

public record CreatePostRequest (
    String title,
    String content,
    Long authorId
){

}