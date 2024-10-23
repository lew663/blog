package com.lew663.blog.domain.article.repository;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findByCategory(Category category);
}
