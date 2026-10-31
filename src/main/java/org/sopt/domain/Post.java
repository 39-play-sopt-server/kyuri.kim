package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;

@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private long viewCount;

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 500;

    protected Post(){}

    private Post(String title, String content, Member member, Category category) {
        validateInitial(member, category);
        validateModifiable(title, content);

        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
    }

    public static Post create(
            String title,
            String content,
            Member member,
            Category category
    ) {
        return new Post(title, content, member, category);
    }

    public void update(String title, String content) {
        validateModifiable(title, content);

        this.title = title;
        this.content = content;
    }

    //바뀔 수 있는 거 검증
    private static void validateModifiable(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_TITLE);
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BusinessException(PostErrorCode.TITLE_TOO_LONG);
        }
        if (content == null || content.isBlank()) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_CONTENT);
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new BusinessException(PostErrorCode.CONTENT_TOO_LONG);
        }
    }

    //바뀔 수 없는 거 검증
    private static void validateInitial(Member member, Category category) {
        if (member == null) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_WRITER);
        }
        if (category == null) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_CATEGORY);
        }
    }

    public boolean isModified(){
        return getUpdatedAt() != null;
    }

    public Long getId() { return id;}
    public String getTitle() { return title;}
    public String getContent() { return content;}
    public Member getMember() { return member;}
    public Category getCategory() { return category;}
    public long getViewCount(){return this.viewCount;}
}