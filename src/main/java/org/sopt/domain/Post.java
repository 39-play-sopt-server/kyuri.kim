package org.sopt.domain;

public class Post {
    private Long id;
    private String title;
    private String content;
    private Member writer;
    private Category category;

    public static Post create(
            String title,
            String content,
            Member writer,
            Category category
    ){
        validate(title, content);

        Post post = new Post();
        post.title = title;
        post.content = content;
        post.writer = writer;
        post.category = category;

        return post;
    }

    /*여기서 고민한 내용 -> id를 어떻게 처리할 것인가.. 왜냐면 수정할 수 있어야 하고 게시글 고유 id가 필요하니까..
    * 위에는 'create'이고, 수정하거나 삭제하거나 등등의 경우에 게시물을 특정할 요소가 필요해서 id를 넣고 -> DB에서 id로 조회할 수 있도록 해야..*/

    public static Post reconstitute(
            Long id,
            String title,
            String content,
            Member writer,
            Category category
    ){
        validate(title, content);

        Post post = new Post();

        post.id = id;
        post.title = title;
        post.content = content;
        post.writer = writer;
        post.category = category;

        return post;
    }

    private Post(){
    }

    public void update(String title, String content){
        validate(title, content);
        this.title = title;
        this.content = content;
    }

    //유효성 검증 메서드를 따로 빼서 작성을 해보자면
    private static void validate(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비워둘 수 없습니다.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 비워둘 수 없습니다.");
        }
    }

    public Long getId(){ return id; }
    public String getTitle(){ //이쪽은 Getter
        return title;
    }
    public String getContent(){ //이쪽도 Getter22
        return content;
    }
    public Member getWriter(){ return writer;}
    public Category getCategory(){ return category;}

}

/*POST에 부여해야 하는 책임이 무엇이 있을까요.? (경민님 세미나를 들으면서 작성함)
저는 가장 먼저 세미나에서 배웠던 1. 캡슐화 (데이터 보호의 책임) 이 떠올랐습니다
title이랑 content를 외부에서 수정할 수가 없겠죠.? 그러면 private으로 처리를 해줍니다

그리고, 게시글의 제목이나 내용을 수정해야 한다면.? Post가 처리하도록 2. 상태 변경 제어하는 책임이 있을 것이구요

제목이나 내용이 비어있는 채로 게시물이 생성될 수 없으니까, 이러한 규칙들을 지켰는지.. 3. 데이터 유효성 검증의 책임이 있을 것 같네요
(제목은 null이면 안된다.. 내용은 200자 제한이 있다.. 등)

나중에 이러한 errorcode, successcode를 세미나에서 다루게 될 텐데..
그러면 나중에는 errorcode나 successcode 디렉토리를 만들어서 enum으로 관리를 하게 될 거에요.!



/*그냥 내가 헷갈려서 정리하는 거
static = 객체 만들지 않아도 사용 가능한 것
static 없으면 = 객체 만든 다음 그 객체를 통해 사용해야 하는 것
void = 반환값이 없음
-> 따라서 update는 static x, validate과 create은 static O
update랑 validate는 void O, create은 void X

static 메서드에서 일반 메서드 호출 불가.. (객체 없어도 실행될 수 있는게 객체가 있어야 실행이 되어야 하는거를 부르면 안되기 때문)
 */