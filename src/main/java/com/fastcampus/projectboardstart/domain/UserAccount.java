package com.fastcampus.projectboardstart.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})

@Entity
public class UserAccount extends AuditingFields{

    @Id
    @Setter
    @Column(length = 50)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String userPassword;

    @Setter
    @Column(length = 100)
    private String email;
    @Setter
    @Column(length = 100)
    private String nickname;
    @Setter
    private String memo;

    protected UserAccount() {

    }

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;

    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo, null);
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo, String createdBy) {
        return new UserAccount(userId, userPassword, email, nickname, memo, createdBy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount userAccount)) return false;
        return userId != null && userId.equals(userAccount.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
