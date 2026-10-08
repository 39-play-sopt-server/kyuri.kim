package org.sopt.application.port.out;

//아웃바운드 포트 (repository 인터페이스) | "게시글을 저장한다"는 인터페이스 정의

import org.sopt.domain.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    //저장
    Post save(Post post);
    //단건 조회
    Optional<Post> findById(Long id);
    //다건 조회
    List<Post> findAll();
    //삭제
    void delete(Long id);
}
