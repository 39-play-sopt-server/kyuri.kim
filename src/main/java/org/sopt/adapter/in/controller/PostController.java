package org.sopt.adapter.in.controller;

//입력 어댑터 - 요청 받아서 유스케이스 호출하고, ApiResponse 반환하는 파일
//외부의 HTTP 요청을 애플리케이션의 인바운드 포트로 전달하는 인바운드 어댑터 파일

import org.sopt.adapter.in.CreatePostRequest;
import org.sopt.adapter.in.dto.PostDetailResponseDto;
import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.application.port.in.PostUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/posts")

public class PostController {
    private final PostUseCase postUseCase;
    public PostController(PostUseCase postUseCase){
        this.postUseCase = postUseCase;
    }

    @PostMapping
    public ResponseEntity<PostDetailResponseDto> createPost(
            @RequestBody CreatePostRequest request){
        CreatePostCommand command = new CreatePostCommand(
                request.title(),
                request.content(),
                request.authorId()
        );

        PostDetailResponseDto response = postUseCase.createPost(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}


/*1. RestController - HTTP 요청 받는 컨트롤러임을 알려주는 어노테이션
-> @Controller (웹 요청 처리하는 Controller라는 뜻) + @ResponseBody ( 메서드의 반환값을 HTTP 응답의 바디에 넣어준다는 뜻)

* 2. RequestBody - HTTP 요청 바디에 들어있는 JSON을 CreatePostRequest 객체로 변환
* */