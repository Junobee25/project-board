package com.fastcampus.projectboardstart.dto.response;

import com.fastcampus.projectboardstart.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleCommentResponse (
        Long id,
        String description,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String description, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleCommentResponse(id, description, createdAt, email, nickname);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.description(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
