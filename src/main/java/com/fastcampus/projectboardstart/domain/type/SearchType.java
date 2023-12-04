package com.fastcampus.projectboardstart.domain.type;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),
    DESCRIPTION("본문"),
    ID("id"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    @Getter private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
