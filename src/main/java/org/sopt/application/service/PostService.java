package org.sopt.application.service;

//입력 포트를 구현하고, 출력포트를 사용하는 파일 -> 유스케이스 구현체

import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.application.port.in.PostUseCase;
import org.sopt.application.port.in.UpdatePostCommand;
import org.sopt.application.port.out.PostRepository;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Post;
import org.sopt.application.port.in.PostPage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements PostUseCase {
    //데이터 저장, 조회 담당하는 아웃바운드 포트
    private final PostRepository postRepository;
    private static final int MAX_PAGE_SIZE = 100;

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
    public PostPage getAllPosts(int page, int size) {
        //검증 -> TODO. 딴데로 옮기기
        if(page<1 || size <1 || size > MAX_PAGE_SIZE){
            throw new BusinessException(PostErrorCode.INVALID_PAGE_REQUEST);
        }
        //데이터 조회
        List<Post> posts = postRepository.findAll(page, size);
        long totalElements = postRepository.count();

        //총 페이지 수 계산
        int totalPages = (int)Math.ceil((double) totalElements/size);

        return new PostPage(posts, page, size, totalElements, totalPages);
    }

    @Override
    public Post getPostById(Long id) {
        return findPostById(id);
    }

    @Override
    public void updatePost(Long id, UpdatePostCommand command) {
        Post post = findPostById(id);
        post.update(command.title(), command.content());
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