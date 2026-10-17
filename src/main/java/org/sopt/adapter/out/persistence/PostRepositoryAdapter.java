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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PostRepositoryAdapter implements PostRepository, LoadPostPort, SavePostPort {
    private final Map<Long, Post> storage = new ConcurrentHashMap<>(); //멀티스레드에서 안전한 해쉬맵으로 변경하기~
    //-> 근데 이러면 정렬 안되니까 정렬 추가하기

    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Post save(Post post) { //동시성 방어
        if (post.getId() == null) {
            Long id = sequence.incrementAndGet();
            Post newPost = Post.reconstitute(
                    id,
                    post.getTitle(),
                    post.getContent(),
                    post.getWriter(),
                    post.getCategory(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    post.getViewCount()
            );
            storage.put(id, newPost);
            return newPost;
        } else {
            storage.put(post.getId(), post);
            return post;
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
    @Override
    public List<Post> findAll(int page, int size, Category category) {
        List<Post> sortedPosts = storage.values().stream()
                .filter(post -> category == null || post.getCategory() == category)
                .sorted(Comparator.comparing(Post::getId))
                .toList();

        int start = (page-1) * size;

        if(start>=sortedPosts.size()){
            return Collections.emptyList();
        }
        int end = Math.min(start+size, sortedPosts.size());
        return new ArrayList<>(sortedPosts.subList(start,end));
    }

    @Override
    public long count(Category category) {
        if (category == null) {
            return storage.size();
        }
        return storage.values().stream()
                .filter(post -> post.getCategory() == category)
                .count();
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }

    @Override
    public Post loadPost(Long postId) {
        Post post = storage.get(postId);
        if (post == null) {
            throw new BusinessException(PostErrorCode.POST_NOT_FOUND);
        }
        return post;
    }

    @Override
    public void savePost(Post post) {
        save(post);
    }
}