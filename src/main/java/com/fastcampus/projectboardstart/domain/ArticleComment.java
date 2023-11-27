package com.fastcampus.projectboardstart.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "description"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Article article; // 게시글 (ID)
    @Setter
    @Column(nullable = false, length = 500)
    private String description; // 본문

    // meta-data
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성 일시
    @CreatedBy
    @Column(nullable = false)
    private String createdBy; // 생성자
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정 일시
    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy; // 수정자

    protected ArticleComment() {
    }

    private ArticleComment(Article article, String description) {
        this.article = article;
        this.description = description;
    }

    public static ArticleComment of(Article article, String description) {
        return new ArticleComment(article, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment articleComment)) return false;
        return id != null && id.equals(articleComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
