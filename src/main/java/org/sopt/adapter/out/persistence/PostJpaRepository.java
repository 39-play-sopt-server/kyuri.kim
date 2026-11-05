package org.sopt.adapter.out.persistence;

import org.sopt.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Override
    @EntityGraph(attributePaths = "member")
    Optional<Post> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "member")
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Modifying
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :id")
    int incrementViewCount(@Param("id") Long id);
}