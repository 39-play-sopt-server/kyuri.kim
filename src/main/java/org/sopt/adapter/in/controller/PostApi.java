package org.sopt.adapter.in.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.adapter.in.dto.CreatePostRequest;
import org.sopt.adapter.in.dto.PostPageResponse;
import org.sopt.adapter.in.dto.PostResponse;
import org.sopt.adapter.in.dto.UpdatePostRequest;
import org.sopt.common.response.ApiResponse;

@Tag(name = "Post", description = "게시글 API")
public interface PostApi {

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    ApiResponse<PostResponse> createPost(CreatePostRequest request);

    @Operation(summary = "게시글 목록 조회", description = "페이징 및 카테고리별 게시글 목록을 조회합니다.")
    ApiResponse<PostPageResponse> getAllPosts(
            @Parameter(description = "페이지 번호", example = "1") int page,
            @Parameter(description = "페이지 사이즈", example = "10") int size,
            @Parameter(description = "카테고리 이름(FREE, INFO, CAREER)", example = "FREE") String category
    );

    @Operation(summary = "게시글 상세 조회", description = "해당 게시글의 상세 정보를 조회하고 조회수를 1 증가시킵니다.")
    ApiResponse<PostResponse> getPostById(Long id);

    @Operation(summary = "게시글 수정", description = "해당 게시글을 수정합니다.")
    ApiResponse<Void> updatePost(
            Long id, UpdatePostRequest request
    );

    @Operation(summary = "게시글 삭제", description = "해당 게시글을 삭제합니다.")
    ApiResponse<Void> deletePost(Long id);
}