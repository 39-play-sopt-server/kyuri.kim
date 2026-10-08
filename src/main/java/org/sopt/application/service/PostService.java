package org.sopt.application.service;

//입력 포트를 구현하고, 출력포트를 사용하는 파일 -> 유스케이스 구현체

import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.application.port.in.PostUseCase;
import org.sopt.application.port.out.PostRepository;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements PostUseCase {
    //데이터 저장, 조회 담당하는 아웃바운드 포트
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    //조회랑 예외 처리 하나로 묶어내기
    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));
    }

    @Override
    public Post createPost(CreatePostCommand command){
        //1. 게시글 생성 정적 팩토리 메서드
        Post post = Post.create(
                command.title(),
                command.content(),
                command.writer(),
                command.category()
        );

        //2. 저장하고 결과 반환
        return postRepository.save(post);
    }

    @Override
    public List<Post>getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return findPostById(id);
    }

    @Override
    public void updatePost(Long id, String title, String content) {
        Post post = findPostById(id);
        post.update(title, content);
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        findPostById(id);
        postRepository.delete(id);
    }
}


/*
* 1. @Service란
* - 서비스 역할을 하는 클래스(위에서는 게시글 생성이라는 비즈니스 로직 처리)임을 스프링에게 알려줌
* -> Bean으로 등록 -> Spring이 알아서 객체 만들어서 넣어줌
* => 의존성 주입 (DI)
*
* 2. @Override란
* - 부모에 있던 메서드를 재정의
* */