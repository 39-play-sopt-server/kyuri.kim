package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Member() {
    }

    private Member(String name) {
        this.name = name;
    }

    public static Member create(String name) {
        return new Member(name);
    }

    public void withdraw() {
        this.deleted = true;
    }

    public String getDisplayName() {
        return deleted ? "탈퇴한 사용자" : name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return deleted;
    }
}