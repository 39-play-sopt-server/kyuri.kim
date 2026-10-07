package org.sopt.adapter.out.persistence;

//출력 어댑터 : HashMap + id 생성하는 파일
//아웃바운드 어댑터 - Outbound Port 구현

import org.sopt.domain.Post;

@Repository
public class PostEntityRespository implements SavePostPort {

    private final SpringDataPostRepository springDataPostRepository;

    public PostEntityRespository(SpringDataPostRepository springDataPostRepository) {
        this.springDataPostRepository = springDataPostRepository;
    }

    @Override
    public Post save(Post domainPost) {
        // 도메인 객체를 JPA 엔티티로 변환 후 저장
        PostJpaEntity jpaEntity = PostJpaEntity.from(domainPost);
        PostJpaEntity savedEntity = springDataPostRepository.save(jpaEntity);
        return savedEntity.toDomain();
    }
}