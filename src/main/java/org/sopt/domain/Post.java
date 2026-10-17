package org.sopt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;

import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long viewCount;

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 500;

    protected Post(){}

    private Post(Long id, String title, String content, String writer, Category category, LocalDateTime createdAt, LocalDateTime updatedAt, long viewCount) {
        validateInitial(writer, category, createdAt);
        validateModifiable(title, content);

        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
        return new Post(null, title, content, writer, category, LocalDateTime.now(), null, 0L);
    }

    /*여기서 고민한 내용 -> id를 어떻게 처리할 것인가.. 왜냐면 수정할 수 있어야 하고 게시글 고유 id가 필요하니까..
     * 위에는 'create'이고, 수정하거나 삭제하거나 등등의 경우에 게시물을 특정할 요소가 필요해서 id를 넣고 -> DB에서 id로 조회할 수 있도록 해야..*/

    //저장된 데이터를 바탕으로 도메인 객체를 복원할 때에만 사용. 새로운 게시글 생성할 때에는 Create() 팩토리 메서드만 사용.
    public static Post reconstitute(
            Long id,
            String title,
            String content,
            String writer,
            Category category,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            long viewCount
    ) {
        return new Post(id, title, content, writer, category, createdAt, updatedAt, viewCount);
    }

    public void update(String title, String content) {
        validateModifiable(title, content);

        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
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
    private static void validateInitial(String writer, Category category, LocalDateTime createdAt) {
        if (writer == null || writer.isBlank()) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_WRITER);
        }
        if (category == null) {
            throw new BusinessException(PostErrorCode.EMPTY_POST_CATEGORY);
        }
        if (createdAt == null) {
            throw new BusinessException(PostErrorCode.INVALID_CREATED_AT);
        }
    }

    public boolean isModified(){
        return this.updatedAt != null;
    }

    public Long getId() { return id;}
    public String getTitle() { return title;}
    public String getContent() { return content;}
    public String getWriter() { return writer;}
    public Category getCategory() { return category;}
    public LocalDateTime getCreatedAt() { return createdAt;}
    public LocalDateTime getUpdatedAt() { return updatedAt;}
    public synchronized long getViewCount(){return this.viewCount;}

}

/* 3주차 내용 정리
* 1. @Entity : 이 클래스가 JPA 엔티티임을 선언
* 2. @Table : 매핑할 테이블 이름 지정
* 3. @Id : 기본키 필드
* 4. @GeneratedValue : 기본키 생성 전략
* 5. @Column : 열 매핑 (이름, NULL 허용, 길이, 유니크)
* 6. @Enumerated : enum 매핑 방식 */