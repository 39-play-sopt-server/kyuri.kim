package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;

@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String writer;

    @Enumerated(EnumType.STRING)
    private Category category;

    private long viewCount;

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 500;

    protected Post(){}

    private Post(Long id, String title, String content, String writer, Category category, long viewCount) {
        validateInitial(writer, category);
        validateModifiable(title, content);

        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.category = category;
        this.viewCount = viewCount;
    }

    public synchronized void increaseViewCount(){ //동시성 방어
        this.viewCount ++;
    }

    public static Post create(
            String title,
            String content,
            String writer,
            Category category
    ) {
        return new Post(null, title, content, writer, category, 0L);
    }

    //저장된 데이터를 바탕으로 도메인 객체를 복원할 때에만 사용. 새로운 게시글 생성할 때에는 Create() 팩토리 메서드만 사용.
    public static Post reconstitute(
            Long id,
            String title,
            String content,
            String writer,
            Category category,
            long viewCount
    ) {
        return new Post(id, title, content, writer, category, viewCount);
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
    private static void validateInitial(String writer, Category category) {
        if (writer == null || writer.isBlank()) {
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
    public String getWriter() { return writer;}
    public Category getCategory() { return category;}
    public synchronized long getViewCount(){return this.viewCount;}

}