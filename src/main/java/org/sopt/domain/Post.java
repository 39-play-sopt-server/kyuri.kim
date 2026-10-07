package org.sopt.domain;

public class Post {
    private String title;
    private String content;

    public Post(String title, String content) {
        //아래의 3번에서 말했던 유효성 검증은 여기 들어가면 되겠죠.!
        validate(title, content);
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content){ //이쪽은 상태 교체 로직
        validate(title, content);
        this.title = title;
        this.content = content;
    }

    //유효성 검증 메서드를 따로 빼서 작성을 해보자면 (+외부에서는 호출할 수 없도록 private으로 작성하는 것이 좋겠죠?)
    private void validate(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비워둘 수 없습니다.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 비워둘 수 없습니다.");
        }
    }

    public String getTitle(){ //이쪽은 Getter
        return title;
    }

    public String getContent(){ //이쪽도 Getter22
        return content;
    }

    //update 로직
    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContent(String content){
        this.content = content;
    }
}

/*POST에 부여해야 하는 책임이 무엇이 있을까요.?
저는 가장 먼저 세미나에서 배웠던 1. 캡슐화 (데이터 보호의 책임) 이 떠올랐습니다
title이랑 content를 외부에서 수정할 수가 없겠죠.? 그러면 private으로 처리를 해줍니다

그리고, 게시글의 제목이나 내용을 수정해야 한다면.? Post가 처리하도록 2. 상태 변경 제어하는 책임이 있을 것이구요

제목이나 내용이 비어있는 채로 게시물이 생성될 수 없으니까, 이러한 규칙들을 지켰는지.. 3. 데이터 유효성 검증의 책임이 있을 것 같네요
(제목은 null이면 안된다.. 내용은 200자 제한이 있다.. 등)

나중에 이러한 errorcode, successcode를 세미나에서 다루게 될 텐데..
그러면 나중에는 errorcode나 successcode 디렉토리를 만들어서 enum으로 관리를 하게 될 거에요.!

*/