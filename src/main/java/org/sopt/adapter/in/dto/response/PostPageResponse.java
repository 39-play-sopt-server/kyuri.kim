package org.sopt.adapter.in.dto.response;

import org.sopt.application.port.in.PostPage;

import java.util.List;

public record PostPageResponse (
    List<PostResponse> posts,
    int page,
    int size,
    long totalElements,
    int totalPages)
{
    public static PostPageResponse from (PostPage postPage){
        List<PostResponse> postResponses = postPage.posts().stream()
                .map(PostResponse::from)
                .toList();

        return new PostPageResponse(
                postResponses,
                postPage.page(),
                postPage.size(),
                postPage.totalElements(),
                postPage.totalPages()
        );
    }
}
