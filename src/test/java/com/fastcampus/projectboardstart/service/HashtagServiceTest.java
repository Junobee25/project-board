package com.fastcampus.projectboardstart.service;

import com.fastcampus.projectboardstart.domain.Hashtag;
import com.fastcampus.projectboardstart.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 해시태그")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @InjectMocks
    private HashtagService sut;

    @Mock
    private HashtagRepository hashtagRepository;

    @MethodSource
    @ParameterizedTest(name = "[{index}] \"{0}\" => {1}")
    @DisplayName("본문을 파싱하면, 해시태그 이름들을 중복 없이 반환한다.")
    void givenDescription_whenParsing_thenReturnsUniqueHashtagNames(String description, Set<String> expected) {
        // Given

        // When
        Set<String> actual = sut.parseHashtagNames(description);

        // Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        then(hashtagRepository).shouldHaveNoMoreInteractions();
    }

    static Stream<Arguments> givenDescription_whenParsing_thenReturnsUniqueHashtagNames() {
        return Stream.of(
                arguments("java", Set.of()),
                arguments("ja#java", Set.of("java")),
                arguments("java#", Set.of()),
                arguments(null, Set.of()),
                arguments("", Set.of()),
                arguments("#java", Set.of("java")),
                arguments("#", Set.of()),
                arguments("#   ", Set.of()),
                arguments("    #", Set.of()),
                arguments("#java#spring", Set.of("java", "spring")),
                arguments("#java   ", Set.of("java")),
                arguments("#java   #spring", Set.of("java", "spring")),
                arguments("#java#스프링 아주 긴 글 ~~~~~~~~~~~~", Set.of("java", "스프링")),
                arguments("아주 긴 글 ~~~~~~~~~~~~#java#스프링", Set.of("java", "스프링"))
        );
    }

    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름에 매칭하는 것들을 중복 없이 반환한다.")
    @Test
    void givenHashtagNames_whenFindingHashtags_thenReturnsHashtagSet() {
        // Given
        Set<String> hashtagNames = Set.of("java", "spring", "boots");
        BDDMockito.given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
                Hashtag.of("java"),
                Hashtag.of("spring")
        ));

        // When
        Set<Hashtag> hashtags = sut.findHashtagsByNames(hashtagNames);

        // Then
        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }
}