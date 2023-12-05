package com.fastcampus.projectboardstart.dto.request;

import com.fastcampus.projectboardstart.dto.ArticleDto;
import com.fastcampus.projectboardstart.dto.UserAccountDto;

public record ArticleRequest(
        String title,
        String description,
        String hashtag
) {

    public static ArticleRequest of(String title, String description, String hashtag) {
        return new ArticleRequest(title, description, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                description,
                hashtag
        );
    }
}
