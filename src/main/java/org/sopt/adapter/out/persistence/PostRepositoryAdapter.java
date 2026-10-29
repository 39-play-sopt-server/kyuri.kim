package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.LoadPostPort;
import org.sopt.application.port.out.PostRepository;
import org.sopt.application.port.out.SavePostPort;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Category;
import org.sopt.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PostRepositoryAdapter implements PostRepository, LoadPostPort, SavePostPort {
    //jpa 추가~ -> 의존성 주입하는 방향으로 해야
    private final PostJpaRepository postJpaRepository;

    public PostRepositoryAdapter(PostJpaRepository postJpaRepository){
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public List<Post> findAll(int page, int size, Category category) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        );

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> result = category == null
                ? postJpaRepository.findAll(pageable)
                : postJpaRepository.findByCategory(category, pageable);

        return result.getContent();
    }

    @Override
    public long count(Category category) {
        return category == null
                ? postJpaRepository.count()
                : postJpaRepository.countByCategory(category);
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }

    @Override
    public Post loadPost(Long postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() ->
                        new BusinessException(PostErrorCode.POST_NOT_FOUND));
    }

    @Override
    public void savePost(Post post) {
        save(post);
    }

    //검색 기능 추가
    @Override
    public List<Post> searchByTitle(
            String title,
            int page,
            int size,
            Category category
    ) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        );

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        String keyword = title.trim();

        Page<Post> result = category == null
                ? postJpaRepository.findByTitleContainingIgnoreCase(keyword, pageable)
                : postJpaRepository.findByCategoryAndTitleContainingIgnoreCase(
                category,
                keyword,
                pageable
        );

        return result.getContent();
    }

    @Override
    public long countByTitle(String title, Category category) {
        String keyword = title.trim();

        return category == null
                ? postJpaRepository.countByTitleContainingIgnoreCase(keyword)
                : postJpaRepository.countByCategoryAndTitleContainingIgnoreCase(
                category,
                keyword
        );
    }
}