package com.fastcampus.projectboardstart.repository;

import com.fastcampus.projectboardstart.domain.Hashtag;
import com.fastcampus.projectboardstart.repository.querydsl.HashtagRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface HashtagRepository extends
        JpaRepository<Hashtag, Long>,
        HashtagRepositoryCustom,
        QuerydslPredicateExecutor<Hashtag> {
    Optional<Hashtag> findByHashtagName(String hastagName);
    List<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
