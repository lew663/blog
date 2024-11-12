package com.lew663.blog.domain.article.repository;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.dto.ArticleSummaryInfo;
import com.lew663.blog.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  // 카테고리로 게시글 검색
  @Query("SELECT new com.lew663.blog.domain.article.dto.ArticleSummaryInfo(a.id, a.title, a.createdDate) " +
      "FROM Article a WHERE a.category = :category")
  Page<ArticleSummaryInfo> findByCategory(Category category, Pageable pageable);

  // 전체 게시글 조회
  @Query("SELECT new com.lew663.blog.domain.article.dto.ArticleSummaryInfo(a.id, a.title, a.createdDate) " +
      "FROM Article a")
  Page<ArticleSummaryInfo> findAllArticles(Pageable pageable);

  // 제목, 내용에 키워드가 포함된 게시글 검색
  @Query("SELECT new com.lew663.blog.domain.article.dto.ArticleSummaryInfo(a.id, a.title, a.createdDate) " +
      "FROM Article a WHERE (a.title LIKE %:keyword% OR a.content LIKE %:keyword%)")
  Page<ArticleSummaryInfo> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
