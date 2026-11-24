package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.common.exception.BusinessException;
import org.sopt.common.exception.MemberErrorCode;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, length = 254)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(unique = true, length = 255)
    private String kakaoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.ROLE_USER;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Member() {
    }

    private static final int MAX_NAME_LENGTH = 50;

    private Member(String name) {
        validateName(name);
        this.name = name;
    }

    public static Member create(String name) {
        return new Member(name);
    }

    public static Member register(String name, String email, String passwordHash) {
        Member member = new Member(name);
        member.email = email;
        member.passwordHash = passwordHash;
        return member;
    }

    public static Member social(String name, String kakaoId) {
        Member member = new Member(name);
        member.kakaoId = kakaoId;
        return member;
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

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public void grantAdmin() {
        this.role = Role.ROLE_ADMIN;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(MemberErrorCode.EMPTY_MEMBER_NAME);
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BusinessException(MemberErrorCode.MEMBER_NAME_TOO_LONG);
        }
    }
}