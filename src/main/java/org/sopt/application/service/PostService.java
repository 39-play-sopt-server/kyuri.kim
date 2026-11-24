package org.sopt.application.service;

//입력 포트를 구현하고, 출력포트를 사용하는 파일 -> 유스케이스 구현체

import org.sopt.application.port.in.*;
import org.sopt.application.port.out.MemberRepository;
import org.sopt.application.port.out.PostRepository;
import org.sopt.application.port.out.PostSearchCondition;
import org.sopt.application.port.out.PostSearchResult;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.MemberErrorCode;
import org.sopt.common.exception.PostErrorCode;
import org.sopt.domain.Member;
import org.sopt.domain.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService implements PostUseCase {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository){
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new BusinessException(PostErrorCode.POST_NOT_FOUND));
    }

    @Override
    @Transactional
    public Post createPost(CreatePostCommand command){
        if (command.memberId() == null) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_WRITER);
        }

        Member member = memberRepository.findById(command.memberId())
                .orElseThrow(() ->
                        new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (member.isDeleted()) {
            throw new BusinessException(MemberErrorCode.MEMBER_WITHDRAWN);
        }

        Post post = Post.create(
                command.title(),
                command.content(),
                member,
                command.category()
        );

        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostPage getAllPosts(GetPostsCommand command) {
        PostSearchCondition condition = new PostSearchCondition(
                command.memberId(), command.category(), command.title());

        PostSearchResult result = postRepository.search(condition, command.page(), command.size());

        int totalPages = (int) Math.ceil((double) result.totalElements() / command.size());
        return new PostPage(result.posts(), command.page(), command.size(),
                result.totalElements(), totalPages);
    }

    @Override
    public Post getPostById(Long id) {
        return findPostById(id);
    }

    @Override
    @Transactional
    public void updatePost(Long id, Long requesterId, UpdatePostCommand command) {
        Post post = findPostById(id);
        validateOwner(post, requesterId);
        post.update(command.title(), command.content());
    }

    @Override
    @Transactional
    public void deletePost(Long id, Long requesterId) {
        Post post = findPostById(id);
        validateOwner(post, requesterId);
        postRepository.deleteById(id);
    }

    private void validateOwner(Post post, Long requesterId) {
        if (!post.getMember().getId().equals(requesterId)) {
            throw new BusinessException(PostErrorCode.NOT_POST_OWNER);
        }
    }
}
