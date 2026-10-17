package org.sopt.adapter.out.persistence;

import org.sopt.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    //TODO. 카테고리 + 페이지네이션 여기에도 놔야 하나? PostRepository에서 분리를 어떤 것을 추가적으로 해와야 하는지?
}
