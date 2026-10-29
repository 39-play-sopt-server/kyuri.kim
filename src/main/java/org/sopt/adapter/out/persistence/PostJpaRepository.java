package org.sopt.adapter.out.persistence;

import org.sopt.domain.Category;
import org.sopt.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = "member")
    Optional<Post> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "member") //게시글 조회할 때, 멤버 정보도 같이 불러오기
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByCategory(Category category, Pageable pageable);

    long countByCategory(Category category);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "member")
    Page<Post> findByCategoryAndTitleContainingIgnoreCase(
            Category category,
            String title,
            Pageable pageable
    );

    long countByTitleContainingIgnoreCase(String title);

    long countByCategoryAndTitleContainingIgnoreCase(
            Category category,
            String title
    );
}
