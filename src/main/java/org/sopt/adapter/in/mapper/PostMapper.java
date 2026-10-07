package org.sopt.adapter.in.mapper;

import org.sopt.domain.Post;
import org.sopt.adapter.out.persistence.entity.PostEntity;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final MemberMapper memberMapper;

    public Post toDomain(PostEntity postEntity){
        return Post.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .writer(memberMapper.toDomain(postEntity.getWriter()))
                .build();
    }

    public PostEntity toEntity(Post post){
        return PostEntity.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .writer(memberMapper.toEntity(post.getWriter()))
                .build();
    }
}