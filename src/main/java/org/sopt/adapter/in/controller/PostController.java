package org.sopt.adapter.in.controller;

//입력 어댑터 - 요청 받아서 유스케이스 호출하고, ApiResponse 반환하는 파일
//외부의 HTTP 요청을 애플리케이션의 인바운드 포트로 전달하는 인바운드 어댑터 파일
//생성, 목록, 상세, 수정, 삭제 기능 담고 -> 도메인 객체를 응답 DTO로 감싸서 ApiResponse에 담아서 반환

import org.sopt.adapter.in.dto.CreatePostRequest;
import org.sopt.adapter.in.dto.PostResponse;
import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.application.port.in.PostUseCase;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.response.ApiResponse;
import org.sopt.domain.Category;
import org.sopt.domain.Post;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component // 스프링 빈으로 등록 -> 이거 안해서 빌드 오류남
public class PostController {
    private final PostUseCase postUseCase;
    public PostController(PostUseCase postUseCase){
        this.postUseCase = postUseCase;
    }

    //globalexceptionhandler 삭제하는 대신 생성한 공통 예외 처리 래퍼 메서드
    private <T> ApiResponse<T> handleRequest(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            return ApiResponse.success(result);
        } catch (BusinessException e) {
            return ApiResponse.fail(e.getErrorCode());
        }
    }

    // 1. 생성
    public ApiResponse<PostResponse> createPost(CreatePostRequest request) {
        return handleRequest(()-> {
            Category category = Category.fromString(request.category());
            CreatePostCommand command = new CreatePostCommand(
                    request.title(),
                    request.content(),
                    request.writer(),
                    category
            );
            Post post = postUseCase.createPost(command);
            return PostResponse.from(post);
        });
    }

    // 2. 목록
    public ApiResponse<List<PostResponse>> getAllPosts() {
        return handleRequest(()->
                postUseCase.getAllPosts().stream()
                    .map(PostResponse::from)
                    .collect(Collectors.toList())
        );
    }

    // 3. 상세
    public ApiResponse<PostResponse> getPostById(Long id) {
        return handleRequest(()-> {
            Post post = postUseCase.getPostById(id);
            return PostResponse.from(post);
        });
    }

    // 4. 수정
    public ApiResponse<Void> updatePost(Long id, CreatePostRequest request){
            return handleRequest(() -> {
                postUseCase.updatePost(id, request.title(), request.content());
                return null;
            });
        }

    // 5. 삭제
    public ApiResponse<Void> deletePost(Long id) {
        return handleRequest(()->{
            postUseCase.deletePost(id);
            return null;
        });
    }
}


/*1. RestController - HTTP 요청 받는 컨트롤러임을 알려주는 어노테이션
-> @Controller (웹 요청 처리하는 Controller라는 뜻) + @ResponseBody ( 메서드의 반환값을 HTTP 응답의 바디에 넣어준다는 뜻)

* 2. RequestBody - HTTP 요청 바디에 들어있는 JSON을 CreatePostRequest 객체로 변환
* -> 근데 JPA 방식 아니니까 다 지움 (globalexception handler 지우면서 다 지웠습니다 -> 콘솔에서 직접 호출하는 구조로 처음에 과제가 주어졌으니까,,*/