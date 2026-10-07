package org.sopt.application.service;

//입력 포트를 구현하고, 출력포트를 사용하는 파일 -> 유스케이스 구현체

import org.sopt.application.port.in.CreatePostCommand;
import org.sopt.application.port.in.PostUseCase;
import org.sopt.application.port.out.SavePostPort;
import org.sopt.domain.Post;

@Service
public class PostService implements PostUseCase {
    private final SavePostPort savePostPort;

    public PostService(SavePostPort savePostPort){
        this.savePostPort = savePostPort;
    }

    @Override
    public PostResponse createPost(CreatePostCommand command){
        //1. 도메인 객체 생성, 비즈니스 검증
        Post post = new Post(command.title(), command.content(), command.authorId());

        //2. Outbound Port를 통한 저장
        Post savedPost = savePostPort.save(post);

        //3. 결과 반환
        return new PostResponse(savedPost.getId(), savedPost.getTitle());
    }

    Member writer = findMemberPort.findById(command.authorId());

    Post post = Post.create(
            command.title(),
            command.content(),
            writer,
            category
    );
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