package com.fastcampus.projectboardstart.dto.request;

import com.fastcampus.projectboardstart.dto.ArticleDto;
import com.fastcampus.projectboardstart.dto.HashtagDto;
import com.fastcampus.projectboardstart.dto.UserAccountDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String description
) {

    public static ArticleRequest of(String title, String description) {
        return new ArticleRequest(title, description);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(
                userAccountDto,
                title,
                description,
                hashtagDtos
        );
    }
}
