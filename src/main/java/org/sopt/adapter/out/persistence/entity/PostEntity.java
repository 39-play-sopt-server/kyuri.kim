package org.sopt.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Table(name = "POST")
@Entity

public class PostEntity {
    @Id
    @GeneratedValue

    private Long id;
    private String title;
    private String content;

    @ManyToOne
    private MemberEntity writer;

    //이거 나중에 NoArgsConstructor로 바꾸면 깔끔~
    protected PostEntity(){
    }

    /*JPA Entity에 기본 생성자 필요 -> JPA가 DB에서 데이터 가져와서 PostEntity 객체 만들 때
    * 기본 생성자 사용하기 때문에... -> protected로 놔서 JPA는 사용 가능하게, 외부에서는 생성 못하게*/

    //이것도 AllArgsConstructor 풀어쓴 거(?)

    public PostEntity(
            Long id,
            String title,
            String content,
            MemberEntity writer
    ){
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public Long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public MemberEntity getWriter(){
        return writer;
    }
}
