package org.sopt.domain;

import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.PostErrorCode;

import java.time.LocalDateTime;

public class Post {
    private final Long id;
    private String title;
    private String content;
    private final String writer;
    private final Category category;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 500;

    private Post(Long id, String title, String content, String writer, Category category, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateInitial(writer, category, createdAt);
        validateModifiable(title, content);

        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Post create(
            String title,
            String content,
            String writer,
            Category category
    ) {
        return new Post(null, title, content, writer, category, LocalDateTime.now(), null);
    }

    /*여기서 고민한 내용 -> id를 어떻게 처리할 것인가.. 왜냐면 수정할 수 있어야 하고 게시글 고유 id가 필요하니까..
     * 위에는 'create'이고, 수정하거나 삭제하거나 등등의 경우에 게시물을 특정할 요소가 필요해서 id를 넣고 -> DB에서 id로 조회할 수 있도록 해야..*/

    public static Post reconstitute(
            Long id,
            String title,
            String content,
            String writer,
            Category category,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Post(id, title, content, writer, category, createdAt, updatedAt);
    }

    public void update(String title, String content) {
        validateModifiable(title, content);

        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    //바뀔 수 있는 거 검증
    private void validateModifiable(String title, String content) {
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
    private void validateInitial(String writer, Category category, LocalDateTime createdAt) {
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
}

///*POST에 부여해야 하는 책임이 무엇이 있을까요.? (경민님 세미나를 들으면서 작성함)
//저는 가장 먼저 세미나에서 배웠던 1. 캡슐화 (데이터 보호의 책임) 이 떠올랐습니다
//title이랑 content를 외부에서 수정할 수가 없겠죠.? 그러면 private으로 처리를 해줍니다
//
//그리고, 게시글의 제목이나 내용을 수정해야 한다면.? Post가 처리하도록 2. 상태 변경 제어하는 책임이 있을 것이구요
//
//제목이나 내용이 비어있는 채로 게시물이 생성될 수 없으니까, 이러한 규칙들을 지켰는지.. 3. 데이터 유효성 검증의 책임이 있을 것 같네요
//(제목은 null이면 안된다.. 내용은 500자 제한이 있다.. 등)
//*/
//
//
///*그냥 내가 헷갈려서 정리하는 거
//static = 객체 만들지 않아도 사용 가능한 것
//static 없으면 = 객체 만든 다음 그 객체를 통해 사용해야 하는 것
//void = 반환값이 없음
//-> 따라서 update는 static x, validate과 create은 static O
//update랑 validate는 void O, create은 void X
//
//static 메서드에서 일반 메서드 호출 불가.. (객체 없어도 실행될 수 있는게 객체가 있어야 실행이 되어야 하는거를 부르면 안되기 때문)
// */