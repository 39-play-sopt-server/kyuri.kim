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

/*세미나 내용 중에 Repository에 어노테이션 붙이는 거 있었는데.. 저는 얘를 인터페이스로 만들고,
* 구현체는 Adapter로 나눠서.. 서비스가 클래스를 직접 의존하는 게 아니라 인터페이스에 의존하는 구조입니다.*/