package org.sopt.application.port.in;

//요청 DTO - 웹 컨트롤러 - 클라한테 받은 게시글 작성 요청 데이터를 안으로 전달하는 DTO
//불변이니까 record 쓰자

import org.sopt.domain.Category;

public record CreatePostCommand (
    String title,
    String content,
    Long writer,
    Category category
){
}
