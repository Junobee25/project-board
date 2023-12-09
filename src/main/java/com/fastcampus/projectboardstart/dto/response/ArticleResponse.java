package com.fastcampus.projectboardstart.dto.response;

import com.fastcampus.projectboardstart.dto.ArticleDto;
import com.fastcampus.projectboardstart.dto.HashtagDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleResponse(
        Long id,
        String title,
        String description,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname
) {

    public static ArticleResponse of(Long id, String title, String description, Set<String> hashtag, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, description, hashtag, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
