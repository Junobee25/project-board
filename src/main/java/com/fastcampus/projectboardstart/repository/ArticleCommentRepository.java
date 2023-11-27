package com.fastcampus.projectboardstart.repository;

import com.fastcampus.projectboardstart.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
