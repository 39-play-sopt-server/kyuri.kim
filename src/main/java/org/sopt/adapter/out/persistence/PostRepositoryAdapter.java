package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.LoadPostPort;
import org.sopt.application.port.out.PostRepository;
import org.sopt.application.port.out.SavePostPort;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Post;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PostRepositoryAdapter implements PostRepository, LoadPostPort, SavePostPort {
    private final Map<Long, Post> storage = new LinkedHashMap<>();

    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Post save(Post post) {
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
    public List<Post> findAll() {
        return new ArrayList<>(storage.values());
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