package com.fastcampus.projectboardstart.dto.request;

import com.fastcampus.projectboardstart.dto.ArticleCommentDto;
import com.fastcampus.projectboardstart.dto.UserAccountDto;

import java.io.Serializable;

/**
 * DTO for {@link com.fastcampus.projectboardstart.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String description) {

    public static ArticleCommentRequest of(Long articleId, String description) {
        return new ArticleCommentRequest(articleId, description);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                articleId,
                userAccountDto,
                description
        );
    }
}