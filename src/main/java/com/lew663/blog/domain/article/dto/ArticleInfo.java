package com.lew663.blog.domain.article.dto;

import com.lew663.blog.domain.article.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleInfo {
  private Long id;
  private String title;
  private String content;
  private String email;
  private Long hits;

  public static ArticleInfo from(Article article) {
    return new ArticleInfo(
        article.getId(),
        article.getTitle(),
        article.getContent(),
        article.getMember().getEmail(),
        article.getHit()
    );
  }
}