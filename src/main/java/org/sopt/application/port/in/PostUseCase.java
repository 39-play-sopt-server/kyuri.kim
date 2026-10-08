package org.sopt.application.port.in;

//인바운드 포트 (유스케이스) | "게시글을 작성한다" (등록, 목록, 상세, 수정, 삭제)
//컨트롤러가 얘를 통해서 기능을 호출함

import org.sopt.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostUseCase {
    //등록
    Post createPost(CreatePostCommand command);
    //목록
    List<Post>getAllPosts();
    //상세
    Optional<Post>getPostBy(Long id);
    //수정
    void updatePost(Long id, String title, String content);
    //삭제
    void deletePost(Long id);
}
