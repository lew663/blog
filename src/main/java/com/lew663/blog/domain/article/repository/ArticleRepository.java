package com.lew663.blog.domain.article.repository;

import com.lew663.blog.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
