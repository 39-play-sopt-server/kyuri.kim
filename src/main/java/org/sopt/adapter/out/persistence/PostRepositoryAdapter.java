package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.LoadPostPort;
import org.sopt.application.port.out.PostRepository;
import org.sopt.application.port.out.SavePostPort;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Category;
import org.sopt.domain.Post;
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
        List<Post> posts = postJpaRepository.findAll().stream()
                .filter(post -> category == null || post.getCategory() == category)
                .sorted(Comparator.comparing(Post::getId))
                .toList();

        int start = (page-1) * size;

        if(start>=posts.size()){
            return Collections.emptyList();
        }
        int end = Math.min(start+size, posts.size());
        return new ArrayList<>(posts.subList(start,end));
    }

    @Override
    public long count(Category category) {
        if (category == null) {
            return postJpaRepository.count();
        }
        return postJpaRepository.findAll().stream()
                .filter(post -> post.getCategory() == category)
                .count();
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
}