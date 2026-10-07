//package org.sopt.adapter.in.mapper;
//-> 원래 하는 김에 member 만들어서 매핑할라 했는데

//생각보다 제 능력 부족 이슈로 기한 안에 못 끝낼 것 같아서.. 때려칠래요... 다음에 할래..

//import org.sopt.domain.Post;
//import org.sopt.adapter.out.persistence.entity.PostEntity;
//
//@Component
//@RequiredArgsConstructor
//public class PostMapper {
//
//    private final MemberMapper memberMapper;
//
//    public Post toDomain(PostEntity postEntity){
//        return Post.builder()
//                .id(postEntity.getId())
//                .title(postEntity.getTitle())
//                .content(postEntity.getContent())
//                .writer(memberMapper.toDomain(postEntity.getWriter()))
//                .build();
//    }
//
//    public PostEntity toEntity(Post post){
//        return PostEntity.builder()
//                .title(post.getTitle())
//                .content(post.getContent())
//                .writer(memberMapper.toEntity(post.getWriter()))
//                .build();
//    }
//}