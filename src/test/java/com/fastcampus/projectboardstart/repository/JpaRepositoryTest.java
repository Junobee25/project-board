package com.fastcampus.projectboardstart.repository;

import com.fastcampus.projectboardstart.domain.Article;
import com.fastcampus.projectboardstart.domain.ArticleComment;
import com.fastcampus.projectboardstart.domain.Hashtag;
import com.fastcampus.projectboardstart.domain.UserAccount;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagRepository hashtagRepository;

    JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository,
            @Autowired HashtagRepository hashtagRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
        this.hashtagRepository = hashtagRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelection_thenWorksFine() {
        // given

        // when
        List<Article> articles = articleRepository.findAll();

        // then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("juno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content");
        article.addHashtags(Set.of(Hashtag.of("spring")));

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // given
        Article article = articleRepository.findById(100L).orElseThrow();
        Hashtag updatedHashtag = Hashtag.of("springboot");
        article.clearHashtags();
        article.addHashtags(Set.of(updatedHashtag));

        // when
        Article savedArticle = articleRepository.saveAndFlush(article);

        // then
        assertThat(savedArticle.getHashtags())
                .hasSize(1)
                .extracting("hashtagName", String.class)
                .containsExactly(updatedHashtag.getHashtagName());
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // given
        Article article = articleRepository.findById(100L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleXCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleXCommentCount - deletedCommentsSize);
    }

    @DisplayName("대댓글 조회 테스트")
    @Test
    void givenParentCommentId_whenSelecting_thenReturnsChildComments() {
        // Given

        // When
        Optional<ArticleComment> parentComment = articleCommentRepository.findById(298L);

        // Then
        assertThat(parentComment).get()
                .hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(0);
    }

    @DisplayName("댓글에 대댓글 삽입 테스트")
    @Test
    void givenParentComment_whenSaving_thenInsertsChildComment() {
        // Given
        ArticleComment parentComment = articleCommentRepository.getReferenceById(298L);
        ArticleComment childComment = ArticleComment.of(
                parentComment.getArticle(),
                parentComment.getUserAccount(),
                "대댓글"
        );

        // When
        parentComment.addChildComment(childComment);
        articleCommentRepository.flush();

        // Then
        assertThat(articleCommentRepository.findById(298L)).get()
                .hasFieldOrPropertyWithValue("parentCommentId", null)
                .extracting("childComments", InstanceOfAssertFactories.COLLECTION)
                .hasSize(1);
    }

    @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트")
    @Test
    void givenArticleCommentHavingChildComments_whenDeletingParentComment_thenDeletesEveryComment() {
        // Given
        ArticleComment parentComment = articleCommentRepository.getReferenceById(298L);
        long previousArticleCommentCount = articleCommentRepository.count();

        // When
        articleCommentRepository.delete(parentComment);

        // Then
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - 1); // 테스트 댓글 + 대댓글 4개
    }

    @DisplayName("댓글 삭제와 대댓글 전체 연동 삭제 테스트 - 댓글 ID + 유저 ID")
    @Test
    void givenArticleCommentIdHavingChildCommentsAndUserId_whenDeletingParentComment_thenDeletesEveryComment() {
        // Given
        long previousArticleCommentCount = articleCommentRepository.count();

        // When
        articleCommentRepository.deleteByIdAndUserAccount_UserId(305L, "uno");

        // Then
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount); // 테스트 댓글 + 대댓글 4개
    }


    @DisplayName("[Querydsl] 전체 hashtag 리스트에서 이름만 조회하기")
    @Test
    void givenNothing_whenQueryingHashtags_thenReturnsHashtagNames() {
        // Given

        // When
        List<String> hashtagNames = hashtagRepository.findAllHashtagNames();

        // Then
        assertThat(hashtagNames).hasSize(15);
    }

//    @DisplayName("[Querydsl] hashtag로 페이징된 게시글 검색하기")
//    @Test
//    void givenHashtagNamesAndPageable_whenQueryingArticles_thenReturnsArticlePage() {
//        // Given
//        List<String> hashtagNames = List.of("blue", "crimson", "Min");
//        Pageable pageable = PageRequest.of(0, 5, Sort.by(
//                Sort.Order.desc("hashtags.hashtagName"),
//                Sort.Order.asc("title")
//        ));
//
//        // When
//        Page<Article> articlePage = articleRepository.findByHashtagNames(hashtagNames, pageable);
//
//        // Then
//        assertThat(articlePage.getContent()).hasSize(pageable.getPageSize());
//        assertThat(articlePage.getContent().get(0).getTitle()).isEqualTo("In sagittis dui vel nisl.");
//        assertThat(articlePage.getContent().get(0).getHashtags())
//                .extracting("hashtagName", String.class)
//                .containsExactly("Min");
//        assertThat(articlePage.getTotalElements()).isEqualTo(17);
//        assertThat(articlePage.getTotalPages()).isEqualTo(4);
//    }

    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("juno");
        }
    }
}
