package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.IncreaseViewCountPort;
import org.sopt.application.port.out.PostRepository;
import org.sopt.application.port.out.PostSearchCondition;
import org.sopt.application.port.out.PostSearchResult;
import org.sopt.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostRepositoryAdapter implements PostRepository, IncreaseViewCountPort {

    private static final Sort LATEST_FIRST = Sort.by(
            Sort.Order.desc("createdAt"),
            Sort.Order.desc("id")
    );

    private final PostJpaRepository postJpaRepository;

    public PostRepositoryAdapter(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public int increaseViewCount(Long postId){
        return postJpaRepository.incrementViewCount(postId);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }

    @Override
    public PostSearchResult search(PostSearchCondition condition, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, LATEST_FIRST);

        Page<Post> result = postJpaRepository.findAll(
                PostSpecifications.search(condition.memberId(), condition.category(), condition.title()),
                pageable
        );

        return new PostSearchResult(result.getContent(), result.getTotalElements());
    }
}