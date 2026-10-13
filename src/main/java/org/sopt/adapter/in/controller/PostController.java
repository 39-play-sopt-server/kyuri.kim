package org.sopt.adapter.in.controller;

//입력 어댑터 - 요청 받아서 유스케이스 호출하고, ApiResponse 반환하는 파일
//외부의 HTTP 요청을 애플리케이션의 인바운드 포트로 전달하는 인바운드 어댑터 파일
//생성, 목록, 상세, 수정, 삭제 기능 담고 -> 도메인 객체를 응답 DTO로 감싸서 ApiResponse에 담아서 반환

import org.sopt.adapter.in.dto.CreatePostRequest;
import org.sopt.adapter.in.dto.PostPageResponse;
import org.sopt.adapter.in.dto.PostResponse;
import org.sopt.adapter.in.dto.UpdatePostRequest;
import org.sopt.application.port.in.*;
import org.sopt.common.response.ApiResponse;
import org.sopt.domain.Category;
import org.sopt.domain.Post;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController implements PostApi {
    private final PostUseCase postUseCase;
    private final IncreasePostViewCountUseCase increasePostViewCountUseCase;

    public PostController(PostUseCase postUseCase, IncreasePostViewCountUseCase increasePostViewCountUseCase) {
        this.postUseCase = postUseCase;
        this.increasePostViewCountUseCase = increasePostViewCountUseCase;
    }

    // 1. 생성
    @Override
    @PostMapping
    public ApiResponse<PostResponse> createPost
    (@RequestBody CreatePostRequest request) {
        Post post = postUseCase.createPost(request.toCommand());
        return ApiResponse.success(PostResponse.from(post));
    }

    // 2. 목록
    @Override
    @GetMapping
    public ApiResponse<PostPageResponse> getAllPosts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "category", required = false) String category)
    {
        Category domainCategory = Category.fromNullableString(category);

        PostPage postPage = postUseCase.getAllPosts(new GetPostsCommand(page, size, domainCategory));
        return ApiResponse.success(PostPageResponse.from(postPage));
    }

    // 3. 상세
    @Override
    @GetMapping (path = "/{postId}")
    public ApiResponse<PostResponse> getPostById(
            @PathVariable(name = "postId") Long id) {
            increasePostViewCountUseCase.increaseViewCount(id);
            Post post = postUseCase.getPostById(id);
            return ApiResponse.success(PostResponse.from(post));
        }

    // 4. 수정
    @Override
    @PutMapping(path = "/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable(name = "postId") Long id, @RequestBody UpdatePostRequest request) {
        postUseCase.updatePost(id, request.toCommand());
        return ApiResponse.success(null);
    }

    // 5. 삭제
    @Override
    @DeleteMapping(path = "/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable(name = "postId") Long id) {
            postUseCase.deletePost(id);
            return ApiResponse.success(null);
        }
}