package org.sopt.adapter.out.persistence;

import org.sopt.application.port.out.PostRepository;
import org.sopt.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

//어댑터 계층을 메모리 방식으로 갈지 JPA 방식으로 갈지 결정하는 과정에서 'hashmap' 사용하라는 과제에 -> HashMap + AtomicLong 사용하기로 했습니당
@Repository
public class PostRepositoryAdapter implements PostRepository {
    //1. 메모리 저장소 (순서 유지를 위해 linkedHashMap 사용하기로..)
    private final Map<Long, Post> storage = new LinkedHashMap<>();

    //2. 동시성 안전한 ID 카운터
    private final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public Post save(Post post) {
        //Post의 id가 null인지 여부로 신규랑 수정을 분류하기
        if(post.getId() == null){
            Long newId = sequence.incrementAndGet(); //sequence에서 새 ID 가져오고

            Post newPost = Post.reconstitute( //id는 final이니까 새로 만들고
                    newId,
                    post.getTitle(),
                    post.getContent(),
                    post.getWriter(),
                    post.getCategory(),
                    post.getCreatedAt(),
                    post.getUpdatedAt()
            );
            //storage에 쌍으로 저장
            storage.put(newId, newPost);
            return newPost;
        } else{
            //id 이미 있으면 덮어쓰기
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
}

/*Map<Long, Post> 이면 [Key, Value] 쌍으로 데이터를 저장하는 구조
*Key는 Long (게시글 ID)이고, Value는 Post(게시글 객체)
* LinkedHashMap하면 전체 목록 조회해서 순서대로 나온다
* AtomicLong은 멀티스레드 환경에서도 원자성 보장해주는 클래스*/