package com.lew663.blog.domain.Article.repository;

import com.lew663.blog.domain.Article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
